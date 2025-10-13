package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class RegisterError extends RuntimeException {
    public RegisterError(String message) {
        super(message);
    }
}
