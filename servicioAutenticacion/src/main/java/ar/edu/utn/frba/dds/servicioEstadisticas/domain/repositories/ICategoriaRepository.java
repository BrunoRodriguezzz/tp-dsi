package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByDetalle(String detalle);
}
