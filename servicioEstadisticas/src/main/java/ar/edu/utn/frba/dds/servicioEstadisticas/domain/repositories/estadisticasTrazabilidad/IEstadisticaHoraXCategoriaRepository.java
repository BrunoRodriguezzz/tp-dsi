package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaHoraXCategoriaRepository extends JpaRepository<EstadisticaHoraXCategoria, Long> {
}
