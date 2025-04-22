package models.entities;

import lombok.Getter;
import lombok.Setter;
import models.entities.enums.EstadoSolicitudEliminacion;
import models.entities.hechos.Hecho;

import java.time.LocalDateTime;

public class SolicitudEliminacion {
    private Hecho hecho;
    private String fundamento;
    @Getter
    private EstadoSolicitudEliminacion estadoSolicitudEliminacion;
    @Setter
    private LocalDateTime fechaResolucion;

    public SolicitudEliminacion(Hecho hecho, String fundamento) {
        this.hecho = hecho;
        if (fundamento.length() < 500) throw new RuntimeException("El fundamento debe tener al menos 500 caracteres");
        this.fundamento = fundamento;
        estadoSolicitudEliminacion = EstadoSolicitudEliminacion.PENDIENTE;
        fechaResolucion = LocalDateTime.now();
    }

    public void serAceptada(){
        estadoSolicitudEliminacion = EstadoSolicitudEliminacion.ACEPTADA;
        this.hecho.eliminar();
    }

    public void serRechazada(){
        estadoSolicitudEliminacion = EstadoSolicitudEliminacion.RECHAZADA;
    }
}