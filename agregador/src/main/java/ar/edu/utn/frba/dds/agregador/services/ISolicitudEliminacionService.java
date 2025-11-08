package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;

import java.util.List;

public interface ISolicitudEliminacionService {
  public List<SolicitudEliminacionOutputDTO> buscarSolicitudes();
  public SolicitudEliminacionOutputDTO guardarSolicitud(SolicitudEliminacionInputDTO solicitud);
  public SolicitudEliminacionOutputDTO rechazarSolicitud(Long idAdministrador, Long idSolicitud);
  public SolicitudEliminacionOutputDTO aceptarSolicitud(Long idAdministrador, Long idSolicitud);
  public SolicitudEliminacionOutputDTO buscarSolicitud(Long id);
}
