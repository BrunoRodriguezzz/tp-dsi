package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;

public interface ISolicitudEliminacionService {
  public SolicitudEliminacionOutputDTO guardar(SolicitudEliminacionInputDTO solicitud);
  public SolicitudEliminacionOutputDTO rechazar(AdministradorInputDTO administrador, Long id);
  public SolicitudEliminacionOutputDTO buscarSolicitud(Long id);
}
