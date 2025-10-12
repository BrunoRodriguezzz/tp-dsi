package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.estadisticasTrazabilidad;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticaProvinciaXCategoriaRepository extends JpaRepository<EstadisticaProvinciaXCategoria, Long> {
}
