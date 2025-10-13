package ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions;

public class ModificationError extends RuntimeException {
    public ModificationError(String message) {
        super(message);
    }
}
