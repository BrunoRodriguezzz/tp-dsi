package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    String nombre;
    String descripcion;
    CriterioInputDTO criterio;
    List<FuenteInputDTO> fuentes;
}
