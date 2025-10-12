package ar.edu.utn.frba.dds.agregador.models.repositories.specifications;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.*;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HechoSpecification {
    public static Specification<Hecho> noEliminado() {
        return (root, query, cb) -> cb.isFalse(root.get("estaEliminado"));
    }

    public static Specification<Hecho> conFiltros(QueryParamsFiltro params) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (params.categoria != null) {
                predicates.add(cb.equal(root.get("categoria").get("titulo"), params.categoria));
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
            if (params.latitud != null) {
                predicates.add(cb.equal(root.get("ubicacion").get("latitud"), params.latitud));
            }
            if (params.longitud != null) {
                predicates.add(cb.equal(root.get("ubicacion").get("longitud"), params.longitud));
            }
            if (params.titulo != null) {
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

    public static Specification<Hecho> conCriterio(Criterio criterio) {
        return (root, query, criteriaBuilder) -> {
            if (criterio == null || criterio.getFiltros() == null || criterio.getFiltros().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            for (EntidadFiltro filtro : criterio.getFiltros()) {
                if (filtro instanceof FiltroCategoria) {
                    FiltroCategoria filtroCat = (FiltroCategoria) filtro;
                    predicates.add(criteriaBuilder.equal(
                            root.get("categoria").get("titulo"),
                            filtroCat.getCategoria().getTitulo()
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
                } else if (filtro instanceof FiltroLatitud) {
                    FiltroLatitud filtroLat = (FiltroLatitud) filtro;
                    predicates.add(criteriaBuilder.equal(
                            root.get("ubicacion").get("latitud"),
                            filtroLat.getLatitud()
                    ));
                } else if (filtro instanceof FiltroLongitud) {
                    FiltroLongitud filtroLon = (FiltroLongitud) filtro;
                    predicates.add(criteriaBuilder.equal(
                            root.get("ubicacion").get("longitud"),
                            filtroLon.getLongitud()
                    ));
                } else if (filtro instanceof FiltroTitulo) {
                    FiltroTitulo filtroTitulo = (FiltroTitulo) filtro;
                    predicates.add(criteriaBuilder.equal(
                            root.get("titulo"),
                            filtroTitulo.getTitulo()
                    ));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
