package ar.edu.utn.frba.dds.fuenteEstatica.models.dto;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import lombok.Data;

import java.util.List;

@Data
public class ArchivoOutputDTO {
    private Long id;
    private String nombre;
    private List<HechoOutputDTO> hechos;
}
