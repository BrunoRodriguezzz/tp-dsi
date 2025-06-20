package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;

public interface IDAOSolicitudEliminacion {
  public SolicitudEliminacion save(SolicitudEliminacion solicitud);
  public SolicitudEliminacion findById(Long id);
}
