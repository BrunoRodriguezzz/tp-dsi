package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Builder
public class SolicitudModOutputDTO {
    private Long          idSolicitud;
    private Long          idHecho;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private Ubicacion     ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private EstadoHecho   estado;

    public static SolicitudModOutputDTO convertir(SolicitudModificacion hecho){
        return SolicitudModOutputDTO
                .builder()
                .idSolicitud(hecho.getId())
                .idHecho(hecho.getIdHecho())
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria().getNombre())
                .contenidoMultimedia(hecho.getContenidoMultimedia().stream().map(ContenidoMultimediaDTO::convertir).toList())
                .ubicacion(hecho.getUbicacion())
                .fechaAcontecimiento(hecho.getFechaAcontecimiento())
                .estado(hecho.getEstadoSolicitud())
                .build();
    }
}