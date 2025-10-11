package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
