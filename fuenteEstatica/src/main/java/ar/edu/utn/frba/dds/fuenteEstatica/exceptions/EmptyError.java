package ar.edu.utn.frba.dds.fuenteEstatica.exceptions;

public class EmptyError extends RuntimeException {

    public EmptyError(String mensaje) {
        super(mensaje);
    }
}
