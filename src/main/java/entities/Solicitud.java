package entities;

import java.time.LocalDateTime;

public class Solicitud {
    private Hecho hecho;
    private String fundamento;
    private EstadoSolicitud estadoSolicitud;
    private LocalDateTime fechaResolucion;

    public Solicitud(Hecho hecho, String fundamento) {
        this.hecho = hecho;
        this.fundamento = fundamento;
        this.estadoSolicitud = EstadoSolicitud.PENDIENTE;
    }
    //TODO Validación del fundamento <=500, por ahora se usa el Constructor
    public void setFundamento(String fundamento){
        if(fundamento.length() <= 500) throw new RuntimeException();
    }

    public Integer serAceptada(){
        estadoSolicitud = EstadoSolicitud.ACEPTADA;
        this.hecho.eliminar();
        return 1;
    }

    public Integer serRechazada(){
        estadoSolicitud = EstadoSolicitud.RECHAZADA;
        return 1;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public EstadoSolicitud getEstadoSolicitud() {
        return estadoSolicitud;
    }
}