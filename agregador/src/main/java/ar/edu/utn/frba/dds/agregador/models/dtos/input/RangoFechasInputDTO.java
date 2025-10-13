package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RangoFechasInputDTO {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
