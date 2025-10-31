package ar.edu.utn.frba.dds.client.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class SolicitudModOutputDTO {
    private Long          idSolicitud;
    private Long          idHecho;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private UbicacionDTO  ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private String        estado;
}
