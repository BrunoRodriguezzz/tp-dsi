package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByTitulo(String titulo);

    @Query("SELECT c FROM Categoria c WHERE LOWER(c.titulo) = LOWER(:titulo)")
    Optional<Categoria> findByTituloIgnoreCase(@Param("titulo") String titulo);

    @Transactional
    default Categoria findByTituloOrCreate(String titulo) {
        Optional<Categoria> categoria = findByTitulo(titulo);
        if (categoria.isPresent()) {
            return categoria.get();
        } else {
            try {
                return save(new Categoria(titulo));
            } catch (DataIntegrityViolationException e) {
                return findByTitulo(titulo)
                    .orElseThrow(() -> new IllegalStateException("Failed to find category after insert failed: " + titulo, e));
            }
        }
    }

    boolean existsByTitulo(String titulo);

    @Query("SELECT c FROM Categoria c WHERE c NOT IN (SELECT cs.categoria FROM CategoriaSinonimo cs)")
    List<Categoria> findCategoriasSinSinonimos();
}
