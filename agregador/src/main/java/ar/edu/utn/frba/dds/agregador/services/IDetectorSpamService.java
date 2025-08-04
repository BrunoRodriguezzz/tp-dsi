package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.SolicitudEliminacion;

public interface IDetectorSpamService {
  public Boolean esSpam(SolicitudEliminacion solicitud);
}
