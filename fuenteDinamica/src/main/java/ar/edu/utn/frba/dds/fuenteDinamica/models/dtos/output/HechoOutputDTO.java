package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class HechoOutputDTO {
    private Long          id;
    private Contribuyente contribuyente;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private Ubicacion     ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private List<String>  etiquetas;
    private String        origen;
    private String        fuente;
    private EstadoHecho   estado;

    public static HechoOutputDTO convertir(Hecho hecho){
        return HechoOutputDTO
                .builder()
                .id(hecho.getId())
                .contribuyente(hecho.getContribuyente())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria().getNombre())
                .contenidoMultimedia(hecho.getContenidoMultimedia().stream().map(ContenidoMultimediaDTO::convertir).toList())
                .ubicacion(hecho.getUbicacion())
                .fechaAcontecimiento(hecho.getFechaAcontecimiento())
                .etiquetas(hecho.getEtiquetas().stream().map(EtiquetaOutputDTO::convertir).toList())
                .origen(hecho.getOrigen())
                .fuente(hecho.getFuente())
                .fechaCarga(hecho.getFechaGuardado())
                .estado(hecho.getEstadoHecho())
                .build();
    }
}