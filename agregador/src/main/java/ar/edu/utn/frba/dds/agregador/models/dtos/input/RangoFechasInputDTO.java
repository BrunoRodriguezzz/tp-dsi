package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RangoFechasInputDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
