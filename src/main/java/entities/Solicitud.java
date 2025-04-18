package entities;

import java.util.Date;

public class Solicitud {
    private Hecho hecho;
    private String fundamento;
    private EstadoSolicitud estadoSolicitud;
    private Date fechaResolucion;

    public Solicitud(Hecho hecho, String fundamento) {
        this.hecho = hecho;
        this.fundamento = fundamento;
        estadoSolicitud = EstadoSolicitud.PENDIENTE;
        fechaResolucion = new Date();
    }
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