package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaProvinciaXColeccionRepository extends JpaRepository<EstadisticaProvinciaXColeccion, Long> {
}
