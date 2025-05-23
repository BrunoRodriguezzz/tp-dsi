package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

public interface ISolicitudEliminacionRepository {
  public SolicitudEliminacion guardar(SolicitudEliminacion solicitud);
  public SolicitudEliminacion buscarSolicitud(Long id);
}
