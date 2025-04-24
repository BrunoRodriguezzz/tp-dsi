package models.entities.solicitudEliminacion;

import lombok.Getter;
import lombok.Setter;
import models.entities.hechos.Hecho;
import models.entities.usuarios.Administrador;
import models.entities.usuarios.Contribuyente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SolicitudEliminacion {
    private Hecho hecho;
    private String fundamento;
    @Getter
    private EstadoSolicitudEliminacion estadoSolicitudEliminacion;
    @Setter
    private LocalDateTime fechaResolucion;
    private List<CambioDeEstadoSolicitud> cambiosEstadoSolicitud;
    private Contribuyente contribuyente;
    private Administrador administrador;

    public SolicitudEliminacion(Hecho hecho, String fundamento, Contribuyente contribuyente) {
        this.hecho = hecho;
        if (fundamento.length() < 500) throw new RuntimeException("El fundamento debe tener al menos 500 caracteres");
        this.fundamento = fundamento;
        this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.PENDIENTE;
        this.fechaResolucion = null;
        this.cambiosEstadoSolicitud = new ArrayList<>();
        this.administrador = null;
        this.contribuyente = contribuyente;
    }

    public void serAceptada(Administrador administrador) {
        this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.ACEPTADA;
        this.fechaResolucion = LocalDateTime.now();
        this.administrador = administrador;

        this.cambiosEstadoSolicitud.add(new CambioDeEstadoSolicitud(this, EstadoSolicitudEliminacion.ACEPTADA));

        this.hecho.eliminar();
    }

    public void serRechazada(Administrador administrador){
        this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.RECHAZADA;
        this.fechaResolucion = LocalDateTime.now();
        this.administrador = administrador;

        this.cambiosEstadoSolicitud.add(new CambioDeEstadoSolicitud(this, EstadoSolicitudEliminacion.RECHAZADA));
    }
}