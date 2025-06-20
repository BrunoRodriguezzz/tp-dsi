package ar.edu.utn.frba.dds.fuenteProxy.models.exceptions;

public class ValidationError extends RuntimeException{

    public ValidationError(String mensaje) {
        super(mensaje);
    }
}
