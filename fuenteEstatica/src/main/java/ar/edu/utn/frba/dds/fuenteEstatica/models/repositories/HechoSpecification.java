package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;  // correcto


public class HechoSpecification {
    public static Specification<HechoEstatica> conFiltro(FiltroEstatica filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getIdHecho() != null) {
                predicates.add(cb.equal(root.get("id"), filtro.getIdHecho()));
            }

            if (filtro.getFecha() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaCreacion"), filtro.getFecha()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<HechoEstatica> nuevos(LocalDateTime fecha, long idArchivo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.greaterThanOrEqualTo(root.get("fechaModificacion"), fecha));
            predicates.add(cb.equal(root.get("idArchivo"), idArchivo));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<HechoEstatica> porIdArchivo(Long idArchivo) {
        return (root, query, cb) -> cb.equal(root.get("idArchivo"), idArchivo);
    }
}
