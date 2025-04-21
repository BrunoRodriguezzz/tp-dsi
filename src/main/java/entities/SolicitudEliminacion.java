package entities;

import java.time.LocalDateTime;

public class SolicitudEliminacion {
    private Hecho hecho;
    private String fundamento;
    private EstadoSolicitudEliminacion estadoSolicitudEliminacion;
    private LocalDateTime fechaResolucion;

    public SolicitudEliminacion(Hecho hecho, String fundamento) {
        this.hecho = hecho;
        this.fundamento = fundamento;
        estadoSolicitudEliminacion = EstadoSolicitudEliminacion.PENDIENTE;
        fechaResolucion = LocalDateTime.now();
    }

    //TODO Validación del fundamento <=500, por ahora se usa el Constructor
    public void setFundamento(String fundamento){
        if(fundamento.length() <= 500) throw new RuntimeException();
    }

    public Integer serAceptada(){
        estadoSolicitudEliminacion = EstadoSolicitudEliminacion.ACEPTADA;
        this.hecho.eliminar();
        return 1;
    }

    public Integer serRechazada(){
        estadoSolicitudEliminacion = EstadoSolicitudEliminacion.RECHAZADA;
        return 1;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public EstadoSolicitudEliminacion getEstadoSolicitudEliminacion() {
        return this.estadoSolicitudEliminacion;
    }
}