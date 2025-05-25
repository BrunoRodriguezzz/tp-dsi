package ar.edu.utn.frba.dds.fuenteDinamica.excepciones;

public class ErrorDeTiempo extends RuntimeException{
    public ErrorDeTiempo(String mensaje){
        super(mensaje);
    }
}
