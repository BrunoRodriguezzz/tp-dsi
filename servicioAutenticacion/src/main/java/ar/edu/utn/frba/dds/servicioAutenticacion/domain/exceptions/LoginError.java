package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class LoginError extends RuntimeException {
    public LoginError(String message) {
        super(message);
    }
}
