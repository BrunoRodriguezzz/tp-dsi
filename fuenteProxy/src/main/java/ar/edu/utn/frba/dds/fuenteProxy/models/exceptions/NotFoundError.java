package ar.edu.utn.frba.dds.fuenteProxy.models.exceptions;

public class NotFoundError extends RuntimeException {

    public NotFoundError(String mensaje) {
        super(mensaje);
    }
}
