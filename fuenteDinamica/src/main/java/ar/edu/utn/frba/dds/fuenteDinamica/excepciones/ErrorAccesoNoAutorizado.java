package ar.edu.utn.frba.dds.fuenteDinamica.excepciones;

public class ErrorAccesoNoAutorizado extends RuntimeException{
    public ErrorAccesoNoAutorizado(String mensaje){
        super(mensaje);
    }
}
