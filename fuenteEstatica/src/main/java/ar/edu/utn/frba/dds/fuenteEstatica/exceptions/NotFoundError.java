package ar.edu.utn.frba.dds.fuenteEstatica.exceptions;

public class NotFoundError extends RuntimeException {

    public NotFoundError(String mensaje) {
        super(mensaje);
    }
}
