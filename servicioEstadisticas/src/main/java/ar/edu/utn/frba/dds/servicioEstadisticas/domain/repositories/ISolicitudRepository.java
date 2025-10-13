package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.SolicitudEliminacion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.EstadoSolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {

    Long countByEstado(EstadoSolicitudEliminacion estado);
}