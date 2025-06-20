package ar.edu.utn.frba.dds.fuenteDinamica.excepciones;

public class ErrorAccesoProhibido extends RuntimeException{
    public ErrorAccesoProhibido(String mensaje){
        super(mensaje);
    }
}
