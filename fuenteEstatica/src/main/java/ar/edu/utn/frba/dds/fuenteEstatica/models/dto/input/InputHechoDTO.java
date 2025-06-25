package ar.edu.utn.frba.dds.fuenteEstatica.models.dto.input;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Origen;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InputHechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private LocalDate fecha_hecho;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Long id_fuente;
    private Long id_hecho;
    private Origen origen;
}
