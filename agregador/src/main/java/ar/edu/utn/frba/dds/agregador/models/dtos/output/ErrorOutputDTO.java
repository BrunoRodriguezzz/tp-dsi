package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorOutputDTO {
  private String message;       // Descripción humana del error
  private String error;         // Tipo de error HTTP, ej: "Bad Request"
  private LocalDateTime timestamp; // Momento exacto del error
}
