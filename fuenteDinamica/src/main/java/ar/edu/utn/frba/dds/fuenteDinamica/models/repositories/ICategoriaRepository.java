package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriaRepository extends JpaRepository<Categoria,Long> {
}
