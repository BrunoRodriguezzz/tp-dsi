package ar.edu.utn.frba.dds.servicioEstadisticas.controllers.advice.exceptions;

public class NotFound extends RuntimeException {
  public NotFound(String message) {
    super(message);
  }
}
