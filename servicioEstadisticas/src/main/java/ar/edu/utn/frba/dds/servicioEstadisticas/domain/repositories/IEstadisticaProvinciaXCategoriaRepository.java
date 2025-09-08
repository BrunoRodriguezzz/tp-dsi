package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaProvinciaXCategoriaRepository extends JpaRepository<EstadisticaProvinciaXCategoria, Long> {
}
