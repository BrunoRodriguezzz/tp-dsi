package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;

@Data
public class ColeccionInputDTO {
    String nombre;
    String descripcion;
    InputCriterioDTO criterio;
    // Donde se consiguen las fuentes.
}
