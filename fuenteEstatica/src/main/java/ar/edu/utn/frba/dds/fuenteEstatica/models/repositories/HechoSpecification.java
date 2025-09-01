package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;  // correcto
import org.springframework.data.jpa.domain.Specification;

public class HechoSpecification {
    public static Specification<HechoEstatica> conFiltro(FiltroEstatica filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getArchivoId() != null) {
                predicates.add(cb.equal(root.get("idArchivo"), filtro.getArchivoId()));
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
