package ar.edu.utn.frba.dds.client.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private String nombre;
    private String descripcion;
    private CriterioInputDTO criterio;
    private List<String> fuentes;
    private List<String> consensos;
}