package ar.edu.utn.frba.dds.agregador.exceptions.dtos;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestExceptionDTO {
  private String message;
  private String error;
  private LocalDateTime timestamp;
}
