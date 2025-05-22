package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import ar.edu.utn.frba.dds.agregador.services.IDetectorSpamService;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

  IAgregadorService agregadorService;

  @Autowired
  ISolicitudEliminacionRepository solicitudEliminacionRepository;
  IDetectorSpamService detectorSpam;

  public SolicitudEliminacionService(IAgregadorService agregadorService, IDetectorSpamService detectorSpam) {
    this.agregadorService = agregadorService;
    this.detectorSpam = detectorSpam;
  }

  public SolicitudEliminacion guardar(SolicitudEliminacionInputDTO solicitudDTO) {
    SolicitudEliminacion solicitud = this.DTOtoSolicitud(solicitudDTO);
    if(this.detectorSpam.esSpam(solicitud)) {
      solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.SPAM);
      return solicitud;
    }
    else{
      SolicitudEliminacion solicitudCreada = this.solicitudEliminacionRepository.guardar(solicitud);
      return solicitudCreada;
    }
  }

  private SolicitudEliminacion DTOtoSolicitud (SolicitudEliminacionInputDTO solicitudDTO){
    Contribuyente contribuyente = null;
    SolicitudEliminacion solicitud = null;
    try{
      contribuyente = new Contribuyente(
          solicitudDTO.getContribuyente().getNombre(),
          solicitudDTO.getContribuyente().getApellido(),
          solicitudDTO.getContribuyente().getFechaNacimiento()
      );
      solicitud = new SolicitudEliminacion(
          this.agregadorService.DTOtoHecho(solicitudDTO.getHecho()),
          solicitudDTO.getFundamento(),
          contribuyente,
          solicitudDTO.getFechaCreacion()
      );
      solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.PENDIENTE);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return solicitud;
  }
}


