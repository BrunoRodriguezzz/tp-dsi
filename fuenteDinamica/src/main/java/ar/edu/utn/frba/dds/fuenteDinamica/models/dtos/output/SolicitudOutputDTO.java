package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Builder
public class SolicitudOutputDTO {
    private Long          idHecho;
    private Contribuyente contribuyente;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private Ubicacion     ubicacion;
    private LocalDate     fechaAcontecimiento;
    private List<String>  etiquetas;
    private String        sugerenciaDeCambio;

    public static SolicitudOutputDTO convertir(Hecho hecho){
        return SolicitudOutputDTO
                .builder()
                .idHecho(hecho.getId())
                .contribuyente(hecho.getContribuyente())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria().getNombre())
                .contenidoMultimedia(hecho.getContenidoMultimedia().stream().map(ContenidoMultimediaDTO::convertir).toList())
                .ubicacion(hecho.getUbicacion())
                .fechaAcontecimiento(hecho.getFechaAcontecimiento())
                .etiquetas(hecho.getEtiquetas().stream().map(EtiquetaOutputDTO::convertir).toList())
                .sugerenciaDeCambio(hecho.getSugerenciaDeCambio())
                .build();
    }
}