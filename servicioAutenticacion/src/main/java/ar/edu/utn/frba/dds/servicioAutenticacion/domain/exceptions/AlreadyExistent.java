package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class AlreadyExistent extends RuntimeException {
    public AlreadyExistent(String message) {
        super(message);
    }
}
