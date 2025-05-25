package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import java.util.List;
import lombok.Data;

@Data
public class FuenteResponseDTO {
  private Long id;
  private String nombre;
  private List<HechoServicioResponseDTO> hechos;
}
