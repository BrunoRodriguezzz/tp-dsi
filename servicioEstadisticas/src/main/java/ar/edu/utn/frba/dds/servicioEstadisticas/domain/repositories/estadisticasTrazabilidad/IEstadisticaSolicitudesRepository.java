package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaSolicitudes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaSolicitudesRepository extends JpaRepository<EstadisticaSolicitudes, Long> {
}
