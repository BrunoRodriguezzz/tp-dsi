package ar.edu.utn.frba.dds.agregador.exceptions.exceptions;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {
  public NotFoundException(String mensaje) {
    super(mensaje);
  }
}
