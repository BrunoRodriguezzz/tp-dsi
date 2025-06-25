package ar.edu.utn.frba.dds.agregador.models.dtos;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.ContribuyenteServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.FuenteResponseDTO;
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
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.*;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.ResolucionSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UtilsDTO {


  // Fuentes
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
