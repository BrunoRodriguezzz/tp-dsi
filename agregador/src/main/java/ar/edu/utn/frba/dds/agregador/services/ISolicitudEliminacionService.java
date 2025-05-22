package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;

public interface ISolicitudEliminacionService {
  public SolicitudEliminacion guardar(SolicitudEliminacionInputDTO solicitud);
}
