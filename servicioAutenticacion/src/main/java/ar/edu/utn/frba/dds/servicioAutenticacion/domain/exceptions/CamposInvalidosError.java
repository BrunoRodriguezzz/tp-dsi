package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class CamposInvalidosError extends RuntimeException {
    public CamposInvalidosError(String message) {
        super(message);
    }
}
