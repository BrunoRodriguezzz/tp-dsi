package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaSolicitudes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaSolicitudesRepository extends JpaRepository<EstadisticaSolicitudes, Long> {
}
