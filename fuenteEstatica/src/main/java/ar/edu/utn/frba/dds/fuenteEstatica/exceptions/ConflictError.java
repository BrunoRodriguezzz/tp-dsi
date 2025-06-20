package ar.edu.utn.frba.dds.fuenteEstatica.exceptions;

public class ConflictError extends RuntimeException {

    public ConflictError(String mensaje) {
        super(mensaje);
    }
}
