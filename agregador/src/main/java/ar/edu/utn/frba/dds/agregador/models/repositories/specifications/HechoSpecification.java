package ar.edu.utn.frba.dds.agregador.models.repositories.specifications;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class HechoSpecification {
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
}
