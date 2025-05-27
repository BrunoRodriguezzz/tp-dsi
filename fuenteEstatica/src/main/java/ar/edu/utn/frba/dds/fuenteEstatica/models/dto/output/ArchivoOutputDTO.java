package ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output;

import lombok.Data;

import java.util.List;

@Data
public class ArchivoOutputDTO {
    private Long id;
    private String nombre;
    private List<HechoOutputDTO> hechos;
}
