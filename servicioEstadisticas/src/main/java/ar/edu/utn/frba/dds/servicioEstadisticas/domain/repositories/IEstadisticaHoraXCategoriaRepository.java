package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaHoraXCategoriaRepository extends JpaRepository<EstadisticaHoraXCategoria, Long> {
}
