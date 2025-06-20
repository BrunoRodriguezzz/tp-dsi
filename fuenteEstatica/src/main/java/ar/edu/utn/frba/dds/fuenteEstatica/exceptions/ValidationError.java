package ar.edu.utn.frba.dds.fuenteEstatica.exceptions;

public class ValidationError extends RuntimeException {

    public ValidationError(String mensaje) {
        super(mensaje);
    }
}
