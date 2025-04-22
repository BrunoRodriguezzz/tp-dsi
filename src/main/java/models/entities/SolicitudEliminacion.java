package models.entities;

import models.entities.enums.EstadoSolicitudEliminacion;
import models.entities.hechos.Hecho;

import java.time.LocalDateTime;

public class SolicitudEliminacion {
    private Hecho hecho;
    private String fundamento;
    private EstadoSolicitudEliminacion estadoSolicitudEliminacion;
    private LocalDateTime fechaResolucion;

    public SolicitudEliminacion(Hecho hecho, String fundamento) {
        this.hecho = hecho;
        if (fundamento.length() < 500) throw new RuntimeException("El fundamento debe tener al menos 500 caracteres");
        this.fundamento = fundamento;
        estadoSolicitudEliminacion = EstadoSolicitudEliminacion.PENDIENTE;
        fechaResolucion = LocalDateTime.now();
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