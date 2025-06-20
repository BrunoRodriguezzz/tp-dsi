package ar.edu.utn.frba.dds.fuenteProxy.models.exceptions;

public class EmptyError extends RuntimeException {
    public EmptyError(String mensaje) {
        super(mensaje);
    }
}
