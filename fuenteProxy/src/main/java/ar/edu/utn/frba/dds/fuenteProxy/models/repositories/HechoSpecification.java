package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HechoSpecification {
    public static Specification<HechoProxy> conFiltro(FiltroProxy filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getFuenteId() != null) {
                predicates.add(cb.equal(root.get("idFuente"), filtro.getFuenteId()));
            }

            if (filtro.getIdHecho() != null) {
                predicates.add(cb.equal(root.get("id"), filtro.getIdHecho()));
            }

            if (filtro.getDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaCreacion"), filtro.getDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
