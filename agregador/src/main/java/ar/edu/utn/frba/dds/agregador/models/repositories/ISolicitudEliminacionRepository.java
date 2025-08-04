package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.SolicitudEliminacion;

public interface ISolicitudEliminacionRepository {
  public SolicitudEliminacion guardarSolicitud(SolicitudEliminacion solicitud);
  public SolicitudEliminacion buscarSolicitud(Long id);
}
