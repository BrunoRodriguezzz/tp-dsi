package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.stereotype.Service;

public interface IDetectorSpamService {
  public Boolean esSpam(SolicitudEliminacion solicitud);
}
