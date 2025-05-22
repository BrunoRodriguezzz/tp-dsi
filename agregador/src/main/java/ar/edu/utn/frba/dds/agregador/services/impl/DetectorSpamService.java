package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.services.IDetectorSpamService;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.stereotype.Service;

@Service
public class DetectorSpamService implements IDetectorSpamService {
  public Boolean esSpam(SolicitudEliminacion solicitud) {
    // TODO: Implementar bonus
    return solicitud.getFundamento().equals("AAA");
  }
}
