package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.AdministradorOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ResolucionSolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import ar.edu.utn.frba.dds.agregador.services.IDetectorSpamService;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.ResolucionSolicitudEliminacion;
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

  @Autowired
  ISolicitudEliminacionRepository solicitudEliminacionRepository;

  public SolicitudEliminacionService(
      IAgregadorService agregadorService,
      IDetectorSpamService detectorSpam,
      IHechoService hechoService
  ) {
    this.agregadorService = agregadorService;
    this.detectorSpam = detectorSpam;
    this.hechoService = hechoService;
  }

  @Override
  public SolicitudEliminacionOutputDTO guardar(SolicitudEliminacionInputDTO solicitudInputDTO) {
    SolicitudEliminacion solicitud = this.DTOtoSolicitud(solicitudInputDTO);
    if(this.detectorSpam.esSpam(solicitud)) {
      solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.SPAM);
      SolicitudEliminacionOutputDTO solicitudSpamDTO = this.SolicitudToDTO(solicitud);
      return solicitudSpamDTO;
    }
    else{
      solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.PENDIENTE);
      SolicitudEliminacion solicitudCreada = this.solicitudEliminacionRepository.guardar(solicitud);
      SolicitudEliminacionOutputDTO solicitudOutputDTO = this.SolicitudToDTO(solicitudCreada);
      return solicitudOutputDTO;
    }
  }

  @Override
  public SolicitudEliminacionOutputDTO rechazar(AdministradorInputDTO administrador, Long id) {
    SolicitudEliminacion solicitud = this.solicitudEliminacionRepository.buscarSolicitud(id);
    try {
      solicitud.serRechazada(this.DTOtoAdministrador(administrador));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    this.solicitudEliminacionRepository.guardar(solicitud);
    SolicitudEliminacionOutputDTO solicitudRechazadaDTO = this.SolicitudToDTO(solicitud);
    return solicitudRechazadaDTO;
  }

  @Override
  public SolicitudEliminacionOutputDTO buscarSolicitud(Long id) {
    SolicitudEliminacion solicitud = this.solicitudEliminacionRepository.buscarSolicitud(id);
    SolicitudEliminacionOutputDTO solicitudDTO = this.SolicitudToDTO(solicitud);
    return solicitudDTO;
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
          this.hechoService.DTOToHecho(solicitudDTO.getHecho()),
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

  private SolicitudEliminacionOutputDTO SolicitudToDTO(SolicitudEliminacion solicitud){
    SolicitudEliminacionOutputDTO solicitudDTO = new SolicitudEliminacionOutputDTO();
    solicitudDTO.setId(solicitud.getId());
    solicitudDTO.setHecho(this.hechoService.HechoToDTO(solicitud.getHecho()));
    solicitudDTO.setFundamento(solicitud.getFundamento());
    solicitudDTO.setFechaCreacion(solicitud.getFechaCreacion());
    solicitudDTO.setContribuyente(this.hechoService.ContribuyenteToDTO(solicitud.getContribuyente()));
    solicitudDTO.setEstado(solicitud.getEstadoSolicitudEliminacion().name());
    if(solicitud.getResolucionSolicitudEliminacion() != null){
      solicitudDTO.setResolucion(this.ResolucionToDTO(solicitud.getResolucionSolicitudEliminacion()));
    }
    return solicitudDTO;
  }

  private Administrador DTOtoAdministrador (AdministradorInputDTO administradorDTO){
    Administrador administrador = new Administrador(
        administradorDTO.getNombre(),
        administradorDTO.getApellido()
    );
    return administrador;
  }

  private AdministradorOutputDTO AdministradorToDTO (Administrador administrador){
    AdministradorOutputDTO administradorDTO = new AdministradorOutputDTO();
    administradorDTO.setNombre(administrador.getNombre());
    administradorDTO.setApellido(administrador.getApellido());
    return administradorDTO;
  }

  private ResolucionSolicitudEliminacionOutputDTO ResolucionToDTO (ResolucionSolicitudEliminacion resolucion) {
    ResolucionSolicitudEliminacionOutputDTO resolucionDTO = new ResolucionSolicitudEliminacionOutputDTO();
    resolucionDTO.setFechaResolucion(resolucion.getFechaResolucion());
    resolucionDTO.setAdministrador(this.AdministradorToDTO(resolucion.getAdministrador()));
    return resolucionDTO;
  }
}


