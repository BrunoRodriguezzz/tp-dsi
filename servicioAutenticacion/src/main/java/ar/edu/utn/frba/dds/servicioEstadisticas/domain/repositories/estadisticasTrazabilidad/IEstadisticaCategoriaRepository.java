package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaCategoriaRepository extends JpaRepository<EstadisticaCategoria, Long> {
}
