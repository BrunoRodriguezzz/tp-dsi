package ar.edu.utn.frba.dds.agregador.models.dtos;

import ar.edu.utn.frba.dds.agregador.models.domain.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.ContribuyenteServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.HechoServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.AdministradorOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ContribuyenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ResolucionSolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.*;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.ResolucionSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Administrador;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UtilsDTO {
  public static ColeccionOutputDTO coleccionToDTO(Coleccion coleccion) {
    ColeccionOutputDTO coleccionDTO = new ColeccionOutputDTO();
    coleccionDTO.setId(coleccion.getId());
    coleccionDTO.setTitulo(coleccion.getTitulo());
    coleccionDTO.setDescripcion(coleccion.getDescripcion());
    coleccionDTO.setHechos(UtilsDTO.mapHechoToDTO(coleccion.getHechos()));
    return coleccionDTO;
  }

  public static List<ColeccionOutputDTO> mapColeccionesToDTO(List<Coleccion> colecciones) {
    List<ColeccionOutputDTO> coleccionesDTO = colecciones.stream().map(UtilsDTO::coleccionToDTO).collect(Collectors.toList());
    return coleccionesDTO;
  }

  public static Hecho DTOToHecho(HechoInputDTO hechoDTO, Contribuyente contribuyente) {
    Hecho hecho;
    try {
      hecho = new Hecho(
          hechoDTO.getTitulo(),
          hechoDTO.getDescripcion(),
          new Categoria(hechoDTO.getCategoria()),
          new Ubicacion(
              hechoDTO.getUbicacion().getLatitud(),
              hechoDTO.getUbicacion().getLongitud()
          ),
          hechoDTO.getFechaAcontecimiento(),
          Origen.valueOf(hechoDTO.getOrigen().toUpperCase())
      );
      hecho.setIdHFuente(hechoDTO.getId());
      hecho.setFuente(hechoDTO.getFuente());
      hecho.setEstaEliminado(false);
      if(contribuyente != null) {
        hecho.setContribuyente(contribuyente);
      }
      return hecho;
    } catch (Exception e){
      System.out.println(e.getMessage());
    }
    return null;
  }

  public static HechoOutputDTO HechoToDTO(Hecho hecho) {
    HechoOutputDTO hechoDTO = new HechoOutputDTO();
    hechoDTO.setId(hecho.getId());
    hechoDTO.setTitulo(hecho.getTitulo());
    hechoDTO.setDescripcion(hecho.getDescripcion());
    hechoDTO.setCategoria(hecho.getCategoria().getTitulo());
    hechoDTO.setUbicacion(UtilsDTO.UbicacionToDTO(hecho.getUbicacion()));
    hechoDTO.setFechaAcontecimiento(hecho.getFechaAcontecimiento());

    if(hecho.getContribuyente() != null){
      hechoDTO.setContribuyente(UtilsDTO.ContribuyenteToDTO(hecho.getContribuyente()));
    }

    else{
      hechoDTO.setContribuyente(null);
    }

    hechoDTO.setFuente(hecho.getFuente());
    hechoDTO.setOrigen(hecho.getOrigen().name());
    return hechoDTO;
  }

  public static List<HechoOutputDTO> mapHechoToDTO(List<Hecho> hechos) {
    if(hechos == null){
      return null;
    }
    List<HechoOutputDTO> hechosDTO =
        hechos.stream()
            .map(UtilsDTO::HechoToDTO)
            .collect(Collectors.toList());
    return hechosDTO;
  }

  public static UbicacionOutputDTO UbicacionToDTO(Ubicacion ubicacion) {
    UbicacionOutputDTO ubicacionDTO = new UbicacionOutputDTO();
    ubicacionDTO.setLatitud(ubicacion.getLatitud());
    ubicacionDTO.setLongitud(ubicacion.getLongitud());
    return ubicacionDTO;
  }

  public static ContribuyenteOutputDTO ContribuyenteToDTO(Contribuyente contribuyente) {
    ContribuyenteOutputDTO contribuyenteDTO = new ContribuyenteOutputDTO();
    contribuyenteDTO.setNombre(contribuyente.getNombre());
    contribuyenteDTO.setApellido(contribuyente.getApellido());
    contribuyenteDTO.setFechaNacimiento(contribuyente.getFechaNacimiento());
    return contribuyenteDTO;
  }

  public static Contribuyente contribuyenteServicioResponseDTOtoContribuyente(ContribuyenteServicioResponseDTO contribuyenteDTO) {
    try{
      Contribuyente contribuyente = new Contribuyente(contribuyenteDTO.getNombre(),contribuyenteDTO.getApellido(),contribuyenteDTO.getFechaNacimiento());
      if(contribuyenteDTO.getId() != null){
        contribuyenteDTO.setId(contribuyenteDTO.getId());
      }
      return contribuyente;
    }catch (Exception e){
      throw new RuntimeException(e.getMessage());
    }
  }

  public static SolicitudEliminacion DTOtoSolicitud (SolicitudEliminacionInputDTO solicitudDTO, Hecho hecho, Contribuyente c){
    Contribuyente contribuyente = null;
    SolicitudEliminacion solicitud = null;
    try{
      contribuyente = new Contribuyente(
          c.getNombre(),
          c.getApellido(),
          c.getFechaNacimiento()
      );
      solicitud = new SolicitudEliminacion(
          hecho,
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

  public static SolicitudEliminacionOutputDTO SolicitudToDTO(SolicitudEliminacion solicitud){
    SolicitudEliminacionOutputDTO solicitudDTO = new SolicitudEliminacionOutputDTO();
    solicitudDTO.setId(solicitud.getId());
    solicitudDTO.setHecho(UtilsDTO.HechoToDTO(solicitud.getHecho()));
    solicitudDTO.setFundamento(solicitud.getFundamento());
    solicitudDTO.setFechaCreacion(solicitud.getFechaCreacion());
    solicitudDTO.setContribuyente(UtilsDTO.ContribuyenteToDTO(solicitud.getContribuyente()));
    solicitudDTO.setEstado(solicitud.getEstadoSolicitudEliminacion().name());
    if(solicitud.getResolucionSolicitudEliminacion() != null){
      solicitudDTO.setResolucion(UtilsDTO.ResolucionToDTO(solicitud.getResolucionSolicitudEliminacion()));
    }
    return solicitudDTO;
  }

  public static Administrador DTOtoAdministrador (AdministradorInputDTO administradorDTO){
    Administrador administrador = new Administrador(
        administradorDTO.getNombre(),
        administradorDTO.getApellido()
    );
    return administrador;
  }

  public static AdministradorOutputDTO AdministradorToDTO (Administrador administrador){
    AdministradorOutputDTO administradorDTO = new AdministradorOutputDTO();
    administradorDTO.setNombre(administrador.getNombre());
    administradorDTO.setApellido(administrador.getApellido());
    return administradorDTO;
  }

  public static ResolucionSolicitudEliminacionOutputDTO ResolucionToDTO (ResolucionSolicitudEliminacion resolucion) {
    ResolucionSolicitudEliminacionOutputDTO resolucionDTO = new ResolucionSolicitudEliminacionOutputDTO();
    resolucionDTO.setFechaResolucion(resolucion.getFechaResolucion());
    resolucionDTO.setAdministrador(UtilsDTO.AdministradorToDTO(resolucion.getAdministrador()));
    return resolucionDTO;
  }


  // Fuentes
  public static Hecho hechoServicioResponseDTOtoHecho(HechoServicioResponseDTO hechoDTO) {
    Hecho hecho;
    try {
      String tituloHecho = hechoDTO.getTitulo();
      hecho = new Hecho(
          tituloHecho,
          hechoDTO.getDescripcion(),
          new Categoria(hechoDTO.getCategoria()),
          new Ubicacion(
              hechoDTO.getUbicacion().getLatitud(),
              hechoDTO.getUbicacion().getLongitud()
          ),
          hechoDTO.getFechaAcontecimiento(),
          Origen.valueOf(hechoDTO.getOrigen().toUpperCase())
      );
      hecho.setIdHFuente(hechoDTO.getId());
      if(hechoDTO.getFuente() != null) {
        hecho.setFuente(hechoDTO.getFuente());
      }
      hecho.setEstaEliminado(false);
      if(hechoDTO.getContribuyente() != null) {
        hecho.setContribuyente(UtilsDTO.contribuyenteServicioResponseDTOtoContribuyente(hechoDTO.getContribuyente()));
      }
      if(hechoDTO.getEtiquetas() != null) {
        hecho.setEtiquetas(hechoDTO.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toList()));
      }
      if(hechoDTO.getContenidoMultimedia() != null) {
        hecho.setContenidoMultimedia(hechoDTO.getContenidoMultimedia().stream().map(ContenidoMultimedia::new).collect(Collectors.toList()));
      }
      return hecho;
    } catch (Exception e){
      throw new RuntimeException(e);
    }
  }

  public static Coleccion inputColeccionToColeccion(ColeccionInputDTO coleccionInputDTO) {
    Coleccion coleccion = new Coleccion(coleccionInputDTO.getNombre(), coleccionInputDTO.getDescripcion());

    // Aca hay unos pares de code smells
    Criterio criterio = new Criterio();
    List<Filtro> filtros = UtilsDTO.crearFiltros(coleccionInputDTO);

    if (filtros.isEmpty()) {
      throw new RuntimeException("Filtros vacios");
    }
    criterio.setFiltros(filtros);
    coleccion.setCriterio(criterio);

    return coleccion;
  }

  public static List<Filtro> crearFiltros(ColeccionInputDTO coleccionInputDTO) {
    List<Filtro> filtros = new ArrayList<>();

    try {
      if (!coleccionInputDTO.getCriterio().getTitulo().isEmpty()) { // Quiero filtrar por título
        FiltroTitulo filtroTitulo = new FiltroTitulo(coleccionInputDTO.getCriterio().getTitulo());
        filtros.add(filtroTitulo);
      }

      if (coleccionInputDTO.getCriterio().getFechaAcontecimiento() != null) { // Quiero filtrar por Fecha
        RangoFechas rangoFechas = new RangoFechas(coleccionInputDTO.getCriterio().getFechaAcontecimiento().getFechaInicio(), coleccionInputDTO.getCriterio().getFechaAcontecimiento().getFechaFin());
        FiltroFechaAcontecimiento filtroFechaAcontecimiento = new FiltroFechaAcontecimiento(rangoFechas);
        filtros.add(filtroFechaAcontecimiento);
      }

      if (!coleccionInputDTO.getCriterio().getCategoria().isEmpty()) { // Quiero filtrar por categoría
        FiltroCategoria filtroCategoria = new FiltroCategoria(new Categoria(coleccionInputDTO.getCriterio().getCategoria()));
        filtros.add(filtroCategoria);
      }

    } catch (Exception e){ throw new RuntimeException(e); }

    return filtros;
  }
}
