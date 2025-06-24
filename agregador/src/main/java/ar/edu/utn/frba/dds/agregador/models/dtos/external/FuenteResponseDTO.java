package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import java.util.List;
import lombok.Data;

@Data
public class FuenteResponseDTO {
  private Long id;
  private String nombre;
  private List<HechoInputDTO> hechos;
}
