package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class NotFoundError extends RuntimeException {
    public NotFoundError(String message) {
        super(message);
    }
}
