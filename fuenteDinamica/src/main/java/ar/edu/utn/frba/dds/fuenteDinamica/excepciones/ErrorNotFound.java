package ar.edu.utn.frba.dds.fuenteDinamica.excepciones;

public class ErrorNotFound extends RuntimeException{
    public ErrorNotFound(String mensaje){
        super(mensaje);
    }
}