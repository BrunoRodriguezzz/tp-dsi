package ar.edu.utn.frba.dds.agregador.exceptions.dtos;

import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HechoYaEliminadoExceptionDTO {
  private String message;
  private String error;
  private LocalDateTime timestamp;
  private HechoOutputDTO hechoEliminado;
}
