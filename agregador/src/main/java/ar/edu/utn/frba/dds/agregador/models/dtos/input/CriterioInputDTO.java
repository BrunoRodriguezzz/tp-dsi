package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;

@Data
public class CriterioInputDTO {
    String titulo;
    RangoFechasInputDTO fechaAcontecimiento;
    String categoria;
}
