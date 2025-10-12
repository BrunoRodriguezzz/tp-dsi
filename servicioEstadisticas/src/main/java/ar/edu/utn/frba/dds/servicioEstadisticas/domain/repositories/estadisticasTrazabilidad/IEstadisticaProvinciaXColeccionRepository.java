package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaProvinciaXColeccionRepository extends JpaRepository<EstadisticaProvinciaXColeccion, Long> {
}
