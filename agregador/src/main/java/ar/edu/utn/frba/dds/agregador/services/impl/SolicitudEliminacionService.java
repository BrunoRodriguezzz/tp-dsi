package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.HechoYaEliminadoException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IAdministradorRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IResolucionSolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.specifications.HechoSpecification;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import ar.edu.utn.frba.dds.agregador.services.IDetectorSpamService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

  IAgregadorService agregadorService;
  IHechoService hechoService;
  IDetectorSpamService detectorSpam;
  IColeccionService coleccionService;
  IFuenteService fuenteService;

  @Autowired
  ISolicitudEliminacionRepository solicitudEliminacionRepository;
  @Autowired
  IContribuyenteRepository contribuyenteRepository;
  @Autowired
  IAdministradorRepository administradorRepository;
  @Autowired
  IResolucionSolicitudEliminacionRepository resolucionSolicitudEliminacionRepository;

  public SolicitudEliminacionService(
      IAgregadorService agregadorService,
      IDetectorSpamService detectorSpam,
      IHechoService hechoService,
      IColeccionService coleccionService,
      IFuenteService fuenteService
  ) {
    this.agregadorService = agregadorService;
    this.detectorSpam = detectorSpam;
    this.hechoService = hechoService;
    this.coleccionService = coleccionService;
    this.fuenteService = fuenteService;
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

    Contribuyente contribuyente = null;
    if(solicitudInputDTO.getIdContribuyente() != null) {
      contribuyente = this.buscarContribuyentePorID(solicitudInputDTO.getIdContribuyente());
    }

    SolicitudEliminacion solicitud = SolicitudEliminacionInputDTO.DTOtoSolicitud(solicitudInputDTO, hecho, contribuyente);
    this.solicitudEliminacionRepository.save(solicitud);
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

  @Transactional
  @Override
  public SolicitudEliminacionOutputDTO rechazarSolicitud(Long idAdministrador, Long idSolicitud) {
    SolicitudEliminacion solicitud = this.buscarSolicitudEliminacionPorID(idSolicitud);

    Administrador administrador = this.buscarAdministradorPorID(idAdministrador);
    if(administrador == null) {
      throw new NotFoundException("No se encontró el administrador de id: " + idAdministrador);
    }

    try {
      solicitud.serRechazada(administrador);
      this.resolucionSolicitudEliminacionRepository.save(solicitud.getResolucionSolicitudEliminacion());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    this.solicitudEliminacionRepository.save(solicitud);
    SolicitudEliminacionOutputDTO solicitudRechazadaDTO = SolicitudEliminacionOutputDTO.SolicitudToDTO(solicitud);
    return solicitudRechazadaDTO;
  }

  @Transactional
  @Override
  public SolicitudEliminacionOutputDTO aceptarSolicitud(Long idAdministrador, Long idSolicitud) {
    SolicitudEliminacion solicitud = this.buscarSolicitudEliminacionPorID(idSolicitud);
    if(solicitud == null) {
      throw new NotFoundException("No se encontró la solicitud de id: " + idSolicitud);
    }

    Administrador administrador = this.buscarAdministradorPorID(idAdministrador);
    if(administrador == null) {
      throw new NotFoundException("No se encontró el administrador de id: " + idAdministrador);
    }

    try {
      solicitud.serAceptada(administrador);
      this.resolucionSolicitudEliminacionRepository.save(solicitud.getResolucionSolicitudEliminacion());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    this.solicitudEliminacionRepository.save(solicitud);
    this.hechoService.guardarHecho(solicitud.getHecho());
    if(solicitud.getEstadoSolicitudEliminacion() == EstadoSolicitudEliminacion.ACEPTADA){
      this.coleccionService.eliminarHechoDeColecciones(solicitud.getHecho());
      this.fuenteService.eliminarHecho(solicitud.getHecho());
    }
    SolicitudEliminacionOutputDTO solicitudAceptadaDTO = SolicitudEliminacionOutputDTO.SolicitudToDTO(solicitud);
    return solicitudAceptadaDTO;
  }

  @Override
  public SolicitudEliminacionOutputDTO buscarSolicitud(Long id) {
    SolicitudEliminacion solicitud = this.buscarSolicitudEliminacionPorID(id);
    SolicitudEliminacionOutputDTO solicitudDTO = SolicitudEliminacionOutputDTO.SolicitudToDTO(solicitud);
    return solicitudDTO;
  }

  @Override
  public List<SolicitudEliminacionOutputDTO> buscarSolicitudes() {
    List<SolicitudEliminacion> solicitudesDominio = this.solicitudEliminacionRepository.findAll();
    List<SolicitudEliminacionOutputDTO> solicitudesOutputDTO = solicitudesDominio.stream().map(SolicitudEliminacionOutputDTO::SolicitudToDTO).toList();
    return solicitudesOutputDTO;
  }

  // ---------------------------------------------------- Privados ----------------------------------------------------
  private SolicitudEliminacionOutputDTO gestionarSolicitudSpam(SolicitudEliminacion solicitud) {
    solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.SPAM);
    this.solicitudEliminacionRepository.save(solicitud);
    SolicitudEliminacionOutputDTO solicitudSpamDTO = SolicitudEliminacionOutputDTO.SolicitudToDTO(solicitud);
    return solicitudSpamDTO;
  }

  private SolicitudEliminacionOutputDTO gestionarSolicitudValida(SolicitudEliminacion solicitud) {
    solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.PENDIENTE);
    SolicitudEliminacion solicitudCreada = this.solicitudEliminacionRepository.save(solicitud);
    SolicitudEliminacionOutputDTO solicitudOutputDTO = SolicitudEliminacionOutputDTO.SolicitudToDTO(solicitudCreada);
    return solicitudOutputDTO;
  }

  private SolicitudEliminacion buscarSolicitudEliminacionPorID(Long id){
    Optional<SolicitudEliminacion> solicitudEliminacionOptional = this.solicitudEliminacionRepository.findById(id);
    if(solicitudEliminacionOptional.isPresent()){
      return solicitudEliminacionOptional.get();
    }
    else {
      throw new NotFoundException("SolicitudEliminacion no encontrada.");
    }
  }

  private Contribuyente buscarContribuyentePorID(Long id){
    Optional<Contribuyente> contribuyenteOptional = this.contribuyenteRepository.findById(id);

    if(contribuyenteOptional.isPresent()){
      return contribuyenteOptional.get();
    }
    else {
      throw new NotFoundException("Contribuyente no encontrado.");
    }
  }

  private Administrador buscarAdministradorPorID(Long id){
    Optional<Administrador> administradorOptional = this.administradorRepository.findById(id);
    if(administradorOptional.isPresent()){
      return administradorOptional.get();
    }
    else {
      throw new NotFoundException("Administrador no encontrado.");
    }
  }
}


