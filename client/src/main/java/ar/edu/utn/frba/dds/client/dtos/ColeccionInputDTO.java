package ar.edu.utn.frba.dds.client.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    private String nombre;
    private String descripcion;
    private CriterioInputDTO criterio;
    private List<NombreFuenteInputDTO> fuentes;
    private List<String> consensos;
}