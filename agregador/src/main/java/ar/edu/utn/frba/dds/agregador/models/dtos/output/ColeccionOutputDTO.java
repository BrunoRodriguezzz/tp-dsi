package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import java.util.List;
import lombok.Data;

@Data
public class ColeccionOutputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private List<HechoOutputDTO> hechos;
}
