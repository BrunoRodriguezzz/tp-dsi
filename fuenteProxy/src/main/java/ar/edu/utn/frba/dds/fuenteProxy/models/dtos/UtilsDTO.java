package ar.edu.utn.frba.dds.fuenteProxy.models.dtos;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Ubicacion;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputFuenteDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UtilsDTO {
    private static final String rutaProxy = "http://localhost:8083";

    public static OutputHecho hechoToDtoOutput(HechoProxy hecho) {
        OutputHecho dto = new OutputHecho();
        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setUbicacion(UtilsDTO.ubicacionToDto(hecho.getUbicacion()));
        dto.setOrigen(hecho.getOrigen());
        dto.setFechaCarga(hecho.getFechaCreacion().toString());

        if (hecho.getFechaHecho() != null)
            dto.setFechaAcontecimiento(hecho.getFechaHecho()); // String?

        //TODO Implementado para pruebas
        dto.setEtiquetas(new ArrayList<>());
        dto.setContenidoMultimedia(new ArrayList<>());

        return dto;
    }

    public static UbicacionDTO ubicacionToDto(Ubicacion ubicacion) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setLatitud(ubicacion.getLatitud());
        dto.setLongitud(ubicacion.getLongitud());
        return dto;
    }

    public static HechoProxy toHechoProxy(InputHecho input) {
        HechoProxy hecho = new HechoProxy(input.getId(), input.getTitulo());
        hecho.setDescripcion(input.getDescripcion());
        hecho.setCategoria(input.getCategoria());
        hecho.establecerUbicacion(input.getLatitud(), input.getLongitud());
        hecho.setFechaHecho(input.getFecha_hecho());
        hecho.setFechaModificacion(input.getUpdated_at());
        hecho.setFechaCreacion(input.getCreated_at());
        hecho.setIdFuente(input.getId_fuente());
        hecho.setEliminado(false);
        hecho.setOrigen(input.getOrigen());

        return hecho;
    }

    public static OutputFuente toOutputFuente(Fuente fuente, List<HechoProxy> hechos) {
        if (hechos == null || hechos.isEmpty()) return null;

        List<OutputHecho> hechosDTO = hechos.stream()
                .map(UtilsDTO::hechoToDtoOutput)
                .collect(Collectors.toList());

        hechosDTO.stream().peek(h -> h.setFuente(fuente.getNombre()));

        OutputFuente output = new OutputFuente();
        output.setId(fuente.getId());
        output.setNombre(fuente.getNombre());
        output.setHechos(hechosDTO);

        return output;
    }

  public static Fuente toFuente(InputFuenteDTO inputFuenteDTO) {
        // TODO: COMO SABEMOS EL TIPO??
        return null;
  }

    public static OutputFuenteAgregador toOutputFuenteAgregador(Fuente fuente) {
        OutputFuenteAgregador output = new OutputFuenteAgregador();
        output.setNombre(fuente.getNombre());
        output.setPath(rutaProxy);
        output.setTipoFuente("PROXY");
        output.setIdInterno(fuente.getId());
        return output;
    }

    public static Coleccion toColeccion(InputColeccionDTO dto) {
        Coleccion coleccion = new Coleccion();
        coleccion.setTitulo(dto.getTitulo());
        coleccion.setDescripcion(dto.getDescripcion());
        coleccion.setCriterio(dto.getCriterio());
        //coleccion.setFuentes(fuentes);
        return coleccion;
    }

    public static OutputColeccionDTO toOutputColeccion(Coleccion coleccion) {
        OutputColeccionDTO dto = new OutputColeccionDTO();
        dto.setId(coleccion.getId());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setCriterio(coleccion.getCriterio());
        dto.setFuentes(coleccion.getFuentes());
        //dto.setHechos(hechos);
        return dto;
    }
}
