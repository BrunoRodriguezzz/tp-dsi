package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.HechoYaEliminadoException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IAdministradorRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import ar.edu.utn.frba.dds.agregador.services.IDetectorSpamService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Administrador;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

  IAgregadorService agregadorService;
  IHechoService hechoService;
  IDetectorSpamService detectorSpam;
  IColeccionService coleccionService;

  @Autowired
  ISolicitudEliminacionRepository solicitudEliminacionRepository;
  @Autowired
  IContribuyenteRepository contribuyenteRepository;
  @Autowired
  IAdministradorRepository administradorRepository;

  public SolicitudEliminacionService(
      IAgregadorService agregadorService,
      IDetectorSpamService detectorSpam,
      IHechoService hechoService,
      IColeccionService coleccionService
  ) {
    this.agregadorService = agregadorService;
    this.detectorSpam = detectorSpam;
    this.hechoService = hechoService;
    this.coleccionService = coleccionService;
  }

  @Override
  public SolicitudEliminacionOutputDTO guardarSolicitud(SolicitudEliminacionInputDTO solicitudInputDTO) {
    Hecho hecho = this.hechoService.buscarHecho(solicitudInputDTO.getIdHecho());
    if(hecho == null) {
      throw new NotFoundException("No se encontró el Hecho de id: " + solicitudInputDTO.getIdHecho());
    }
    if(hecho.getEstaEliminado()) {
      throw new HechoYaEliminadoException("El Hecho de id: " + hecho.getId() + " ya ha sido eliminado", hecho);
    }

    Contribuyente contribuyente = this.contribuyenteRepository.buscarContribuyente(solicitudInputDTO.getIdContribuyente());
    if(contribuyente == null) {
      throw new NotFoundException("No se encontró el contribuyente de id: " + solicitudInputDTO.getIdContribuyente());
    }

    SolicitudEliminacion solicitud = UtilsDTO.DTOtoSolicitud(solicitudInputDTO, hecho, contribuyente);

    if(this.detectorSpam.esSpam(solicitud)) {
      // Es marcada como spam y no se guarda, solo se devuelve
      SolicitudEliminacionOutputDTO solicitudEliminacionOutputDTO =  this.gestionarSolicitudSpam(solicitud);
      return solicitudEliminacionOutputDTO;
    }
    else{
      SolicitudEliminacionOutputDTO solicitudEliminacionOutputDTO =  this.gestionarSolicitudValida(solicitud);
      return solicitudEliminacionOutputDTO;
    }
  }

  @Override
  // TODO: Emprolijar codigo
  public SolicitudEliminacionOutputDTO rechazarSolicitud(Long idAdministrador, Long idSolicitud) {
    SolicitudEliminacion solicitud = this.solicitudEliminacionRepository.buscarSolicitud(idSolicitud);
    if(solicitud == null) {
      throw new NotFoundException("No se encontró la solicitud de id: " + idSolicitud);
    }

    Administrador administrador = this.administradorRepository.buscarAdministrador(idAdministrador);
    if(administrador == null) {
      throw new NotFoundException("No se encontró el administrador de id: " + idAdministrador);
    }

    try {
      solicitud.serRechazada(administrador);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    this.solicitudEliminacionRepository.guardarSolicitud(solicitud);
    SolicitudEliminacionOutputDTO solicitudRechazadaDTO = UtilsDTO.SolicitudToDTO(solicitud);
    return solicitudRechazadaDTO;
  }

  @Override
  // TODO: Emprolijar codigo
  public SolicitudEliminacionOutputDTO aceptarSolicitud(Long idAdministrador, Long idSolicitud) {
    SolicitudEliminacion solicitud = this.solicitudEliminacionRepository.buscarSolicitud(idSolicitud);
    if(solicitud == null) {
      throw new NotFoundException("No se encontró la solicitud de id: " + idSolicitud);
    }

    Administrador administrador = this.administradorRepository.buscarAdministrador(idAdministrador);
    if(administrador == null) {
      throw new NotFoundException("No se encontró el administrador de id: " + idAdministrador);
    }

    try {
      solicitud.serAceptada(administrador);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    this.solicitudEliminacionRepository.guardarSolicitud(solicitud);
    this.hechoService.guardarHecho(solicitud.getHecho());
    if(solicitud.getEstadoSolicitudEliminacion() == EstadoSolicitudEliminacion.ACEPTADA){
      this.coleccionService.eliminarHechoDeColecciones(solicitud.getHecho());
    }
    SolicitudEliminacionOutputDTO solicitudAceptadaDTO = UtilsDTO.SolicitudToDTO(solicitud);
    return solicitudAceptadaDTO;
  }

  @Override
  public SolicitudEliminacionOutputDTO buscarSolicitud(Long id) {
    SolicitudEliminacion solicitud = this.solicitudEliminacionRepository.buscarSolicitud(id);
    SolicitudEliminacionOutputDTO solicitudDTO = UtilsDTO.SolicitudToDTO(solicitud);
    return solicitudDTO;
  }

  // ---------------------------------------------------- Privados ----------------------------------------------------
  private SolicitudEliminacionOutputDTO gestionarSolicitudSpam(SolicitudEliminacion solicitud) {
    solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.SPAM);
    SolicitudEliminacionOutputDTO solicitudSpamDTO = UtilsDTO.SolicitudToDTO(solicitud);
    return solicitudSpamDTO;
  }

  private SolicitudEliminacionOutputDTO gestionarSolicitudValida(SolicitudEliminacion solicitud) {
    solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.PENDIENTE);
    SolicitudEliminacion solicitudCreada = this.solicitudEliminacionRepository.guardarSolicitud(solicitud);
    SolicitudEliminacionOutputDTO solicitudOutputDTO = UtilsDTO.SolicitudToDTO(solicitudCreada);
    return solicitudOutputDTO;
  }
}


