package ar.edu.utn.frba.dds.fuenteEstatica.models.dto;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.input.InputHechoDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputAgregadorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.UbicacionDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Ubicacion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

public class UtilsDTO {
    @Value("${servicio.estatica}")
    private static String path;

    public static ArchivoOutputDTO toOutputArchivo(Archivo archivo, List<HechoEstatica> hechos) {
        if (hechos == null || hechos.isEmpty()) return null;

        List<HechoOutputDTO> hechosDTO = hechos.stream()
                .map(UtilsDTO::hechoToOutputDTO)
                .collect(Collectors.toList());

        ArchivoOutputDTO output = new ArchivoOutputDTO();
        output.setId(archivo.getId());
        output.setNombre(archivo.getNombre());
        output.setHechos(hechosDTO);

        return output;
    }

    public static HechoOutputDTO hechoToOutputDTO(HechoEstatica hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setUbicacion(UtilsDTO.ubicacionToDto(hecho.getUbicacion()));
        dto.setOrigen(hecho.getOrigen());
        dto.setContribuyente(null);
        dto.setFuente(null);
        dto.setFechaCarga(hecho.getFechaCreacion().toString());
        dto.setContenidoMultimedia(new ArrayList<>());
        dto.setEtiquetas(new ArrayList<>());

        if (hecho.getFechaHecho() != null)
            dto.setFechaAcontecimiento(hecho.getFechaHecho().toString());
//        if (hecho.getFechaCreacion() != null)
//            dto.setCreated_at(hecho.getFechaCreacion().toString());
//        if (hecho.getFechaModificacion() != null)
//            dto.setUpdated_at(hecho.getFechaModificacion().toString());
        return dto;
    }

    public static UbicacionDTO ubicacionToDto(Ubicacion ubicacion) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setLatitud(ubicacion.getLatitud());
        dto.setLongitud(ubicacion.getLongitud());
        return dto;
    }

    public static HechoEstatica toHechoEstica(InputHechoDTO input) {
        HechoEstatica hecho = new HechoEstatica();
        hecho.setId(input.getId_hecho());
        hecho.setTitulo(input.getTitulo());
        hecho.setDescripcion(input.getDescripcion());
        hecho.setCategoria(input.getCategoria());
        hecho.establecerUbicacion(input.getLatitud(), input.getLongitud());
        hecho.setFechaHecho(input.getFecha_hecho());
        hecho.setFechaModificacion(input.getUpdated_at());
        hecho.setFechaCreacion(input.getCreated_at());
        hecho.setIdArchivo(input.getId_fuente());
        hecho.setEliminado(false);
        hecho.setOrigen(input.getOrigen());

        return hecho;
    }


    public static ArchivoOutputAgregadorDTO toOutputArchivoAgregador(Archivo archivo) {
        ArchivoOutputAgregadorDTO dto = new ArchivoOutputAgregadorDTO();
        dto.setNombre(archivo.getNombre());
        dto.setIdInterno(archivo.getId());
        dto.setTipoFuente("ESTATICA");
        dto.setPath(path);

        return dto;
    }
}
