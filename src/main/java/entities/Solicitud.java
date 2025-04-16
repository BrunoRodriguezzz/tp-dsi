package entities;

public class Solicitud {
    private Hecho hecho;
    private String fundamento;
    private EstadoSolicitud estadoSolicitud;

    public Integer serAceptada(){
        estadoSolicitud = EstadoSolicitud.ACEPTADA;
        this.hecho.eliminar();
        return 1;
    }

    public Integer serRechazada(){
        estadoSolicitud = EstadoSolicitud.RECHAZADA;
        return 1;
    }
}