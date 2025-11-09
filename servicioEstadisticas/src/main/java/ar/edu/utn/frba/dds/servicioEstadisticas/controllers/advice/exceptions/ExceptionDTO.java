package ar.edu.utn.frba.dds.servicioEstadisticas.controllers.advice.exceptions;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDTO {
  private String message;
  private String error;
  private LocalDateTime timestamp;
}
