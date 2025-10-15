package ar.edu.utn.frba.dds.client.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private CriterioInputDTO criterio;
    private List<NombreFuenteInputDTO> fuentes;
    private List<String> consensos;
}