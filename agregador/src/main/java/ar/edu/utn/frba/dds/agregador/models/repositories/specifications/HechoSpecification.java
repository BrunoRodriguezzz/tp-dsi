package ar.edu.utn.frba.dds.agregador.models.repositories.specifications;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.*;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.HechoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HechoSpecification {

    public static Specification<Hecho> perteneceAColeccion(Long coleccionId) {
        return (root, query, cb) -> {
            var subquery = query.subquery(Long.class);
            var coleccionRoot = subquery.from(Coleccion.class);
            var hechosJoin = coleccionRoot.join("hechos");

            subquery.select(hechosJoin.get("id"))
                    .where(cb.equal(coleccionRoot.get("id"), coleccionId));

            return root.get("id").in(subquery);
        };
    }

    public static Specification<Hecho> tieneConsensos(List<Consenso> consensosRequeridos) {
        return (root, query, cb) -> {
            if (consensosRequeridos == null || consensosRequeridos.isEmpty()) {
                return cb.conjunction(); // No filter if no consensos
            }

            // El hecho debe tener TODOS los consensos requeridos
            List<Predicate> predicates = new ArrayList<>();
            for (Consenso consenso : consensosRequeridos) {
                predicates.add(cb.isMember(consenso, root.get("consensos")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Hecho> noEliminado() {
        return (root, query, cb) -> cb.isFalse(root.get("estaEliminado"));
    }

    public static Specification<Hecho> conFiltros(QueryParamsFiltro params) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            final int radio = 10; // Radio en kilometros

            if (params.categoria != null && !params.categoria.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("categoria").get("titulo")), params.categoria.toLowerCase()));
            }

            if (params.fuente != null && !params.fuente.isEmpty()) {
                Join<Hecho, HechoFuente> hechoFuenteJoin = root.join("fuenteSet");
                predicates.add(cb.equal(hechoFuenteJoin.get("fuente").get("nombre"), params.fuente));
            }

            if (params.fechaAcontecimientoInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaAcontecimiento"), params.fechaAcontecimientoInicio));
            }
            if (params.fechaAcontecimientoFin != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaAcontecimiento"), params.fechaAcontecimientoFin));
            }
            if (params.fechaCargaInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaCarga"), params.fechaCargaInicio));
            }
            if (params.fechaCargaFin != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaCarga"), params.fechaCargaFin));
            }
            if (params.latitud != null && params.longitud != null) {
                double latitudEnRadianes = Math.toRadians(params.latitud);

                // 1 grado de latitud son aprox 111.1 km
                double deltaLat = radio / 111.1;

                // 1 grado de longitud es aprox 111.1 * cos(latitud) km
                double deltaLon = radio / (111.1 * Math.cos(latitudEnRadianes));

                double latMin = params.latitud - deltaLat;
                double latMax = params.latitud + deltaLat;
                double lonMin = params.longitud - deltaLon;
                double lonMax = params.longitud + deltaLon;

                predicates.add(cb.between(root.get("ubicacion").get("latitud"), latMin, latMax));
                predicates.add(cb.between(root.get("ubicacion").get("longitud"), lonMin, lonMax));
            } else if (params.latitud != null) {
                predicates.add(cb.equal(root.get("ubicacion").get("latitud"), params.latitud));
            } else if (params.longitud != null) {
                predicates.add(cb.equal(root.get("ubicacion").get("longitud"), params.longitud));
            }
            if (params.titulo != null && !params.titulo.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("titulo")), "%" + params.titulo.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Hecho> conFuentes(List<Fuente> fuentes) {
        return (root, query, criteriaBuilder) -> {
            if (fuentes == null || fuentes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> fuenteJoin = root.join("fuenteSet");

            Path<Fuente> fuentePath = fuenteJoin.get("fuente");

            query.distinct(true);

            return fuentePath.in(fuentes);
        };
    }

    public static Specification<Hecho> conFuentesIds(List<Long> fuenteIds) {
        return (root, query, criteriaBuilder) -> {
            if (fuenteIds == null || fuenteIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Hecho, HechoFuente> hechoFuenteJoin = root.join("fuenteSet");
            Path<Long> fuenteIdPath = hechoFuenteJoin.get("fuente").get("id");
            query.distinct(true);
            return fuenteIdPath.in(fuenteIds);
        };
    }

    public static Specification<Hecho> conCriterio(Criterio criterio) {
        return (root, query, criteriaBuilder) -> {
            if (criterio == null || criterio.getFiltros() == null || criterio.getFiltros().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            final int radio = 10; // Radio en kilometros

            FiltroLatitud filtroLat = criterio.getFiltros().stream()
                    .filter(FiltroLatitud.class::isInstance)
                    .map(FiltroLatitud.class::cast)
                    .findFirst().orElse(null);

            FiltroLongitud filtroLon = criterio.getFiltros().stream()
                    .filter(FiltroLongitud.class::isInstance)
                    .map(FiltroLongitud.class::cast)
                    .findFirst().orElse(null);

            for (EntidadFiltro filtro : criterio.getFiltros()) {
                if (filtro instanceof FiltroLatitud || filtro instanceof FiltroLongitud) {
                    continue;
                }
                if (filtro instanceof FiltroCategoria) {
                    FiltroCategoria filtroCat = (FiltroCategoria) filtro;
                    predicates.add(criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("categoria").get("titulo")),
                            filtroCat.getCategoria().getTitulo().toLowerCase()
                    ));
                } else if (filtro instanceof FiltroEtiqueta) {
                    FiltroEtiqueta filtroEtiqueta = (FiltroEtiqueta) filtro;
                    predicates.add(criteriaBuilder.isMember(
                            filtroEtiqueta.getEtiqueta(),
                            root.get("etiquetas")
                    ));
                } else if (filtro instanceof FiltroFechaAcontecimientoFinal) {
                    FiltroFechaAcontecimientoFinal filtroFecha = (FiltroFechaAcontecimientoFinal) filtro;
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get("fechaCarga"),
                            filtroFecha.getFechaFinal()
                    ));
                } else if (filtro instanceof FiltroFechaCargaFinal) {
                    FiltroFechaCargaFinal filtroFecha = (FiltroFechaCargaFinal) filtro;
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get("fechaCarga"),
                            filtroFecha.getFechaFinal()
                    ));
                } else if (filtro instanceof FiltroFechaCargaInicio) {
                    FiltroFechaCargaInicio filtroFecha = (FiltroFechaCargaInicio) filtro;
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get("fechaCarga"),
                            filtroFecha.getFechaInicio()
                    ));
                } else if (filtro instanceof FiltroTitulo) {
                    FiltroTitulo filtroTitulo = (FiltroTitulo) filtro;
                    predicates.add(criteriaBuilder.equal(
                            root.get("titulo"),
                            filtroTitulo.getTitulo()
                    ));
                }
            }

            if (filtroLat != null && filtroLon != null) {
                double latitud = filtroLat.getLatitud();
                double longitud = filtroLon.getLongitud();
                double latitudEnRadianes = Math.toRadians(latitud);

                // 1 grado de latitud son aprox 111.1 km
                double deltaLat = radio / 111.1;

                // 1 grado de longitud es aprox 111.1 * cos(latitud) km
                double deltaLon = radio / (111.1 * Math.cos(latitudEnRadianes));

                double latMin = latitud - deltaLat;
                double latMax = latitud + deltaLat;
                double lonMin = longitud - deltaLon;
                double lonMax = longitud + deltaLon;

                predicates.add(criteriaBuilder.between(root.get("ubicacion").get("latitud"), latMin, latMax));
                predicates.add(criteriaBuilder.between(root.get("ubicacion").get("longitud"), lonMin, lonMax));
            } else if (filtroLat != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("ubicacion").get("latitud"),
                        filtroLat.getLatitud()
                ));
            } else if (filtroLon != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("ubicacion").get("longitud"),
                        filtroLon.getLongitud()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Hecho> conColeccionId(Long coleccionId) {
        return (root, query, criteriaBuilder) -> {
            if (coleccionId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Hecho, ?> coleccionJoin = root.join("colecciones");
            query.distinct(true);
            return criteriaBuilder.equal(coleccionJoin.get("id"), coleccionId);
        };
    }
}
