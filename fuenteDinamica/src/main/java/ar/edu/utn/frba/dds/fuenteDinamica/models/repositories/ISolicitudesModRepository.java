package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.SolicitudModificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ISolicitudesModRepository extends JpaRepository<SolicitudModificacion, Long> {
    Optional<SolicitudModificacion> findById(Long id);
    List<SolicitudModificacion> findByEstadoSolicitud(EstadoHecho estadoHecho);
}
