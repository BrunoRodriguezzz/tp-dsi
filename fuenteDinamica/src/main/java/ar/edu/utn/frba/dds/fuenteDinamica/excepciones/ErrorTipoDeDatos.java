package ar.edu.utn.frba.dds.fuenteDinamica.excepciones;

public class ErrorTipoDeDatos extends RuntimeException{
    public ErrorTipoDeDatos(String mensaje){
        super(mensaje);
    }
}
