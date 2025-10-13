package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class InvalidPasswordError extends RuntimeException {
    public InvalidPasswordError(String message) {
        super(message);
    }
}
