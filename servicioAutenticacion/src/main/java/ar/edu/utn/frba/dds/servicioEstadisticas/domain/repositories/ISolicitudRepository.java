package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.SolicitudEliminacion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.EstadoSolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {

    Long countByEstado(EstadoSolicitudEliminacion estado);
}