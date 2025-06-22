package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;

@Data
public class InputCriterioDTO {
    String titulo;
    InputRangoFechas fechaAcontecimiento;
    String categoria;
}
