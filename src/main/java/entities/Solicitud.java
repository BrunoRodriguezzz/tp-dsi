package entities;

import java.util.Date;

public class Solicitud {
    private Hecho hecho;
    private String fundamento;
    private EstadoSolicitud estadoSolicitud;
    private Date fechaResolucion;

    public Integer serAceptada(){
        estadoSolicitud = EstadoSolicitud.ACEPTADA;
        this.fechaResolucion = new Date();
        this.hecho.eliminar();
        return 1;
    }

    public Integer serRechazada(){
        estadoSolicitud = EstadoSolicitud.RECHAZADA;
        this.fechaResolucion = new Date();
        return 1;
    }
}