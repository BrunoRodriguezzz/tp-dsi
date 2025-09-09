package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
    Optional<Coleccion> findByDetalle(String detalle);
}
