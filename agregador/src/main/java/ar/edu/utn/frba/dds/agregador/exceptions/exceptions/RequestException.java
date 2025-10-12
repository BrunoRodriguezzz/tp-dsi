package ar.edu.utn.frba.dds.agregador.exceptions.exceptions;

import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import lombok.Data;

@Data
public class RequestException extends RuntimeException {
  public RequestException(String mensaje) {
    super(mensaje);
  }
}
