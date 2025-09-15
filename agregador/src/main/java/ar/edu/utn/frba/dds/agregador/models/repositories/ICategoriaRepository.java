package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByTitulo(String titulo);

    @Query("SELECT c FROM Categoria c WHERE LOWER(c.titulo) = LOWER(:titulo)")
    Optional<Categoria> findByTituloIgnoreCase(@Param("titulo") String titulo);

    boolean existsByTitulo(String titulo);
}
