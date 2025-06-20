package ar.edu.utn.frba.dds.fuenteProxy.models.exceptions;

public class ConflictError extends RuntimeException {

    public ConflictError(String mensaje) {
        super(mensaje);
    }
}
