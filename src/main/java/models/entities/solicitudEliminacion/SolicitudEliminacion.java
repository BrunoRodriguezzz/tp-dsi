package models.entities.solicitudEliminacion;

import lombok.Getter;
import lombok.Setter;
import models.entities.hechos.Hecho;
import models.entities.usuarios.Administrador;
import models.entities.usuarios.Contribuyente;
import models.entities.utils.Errores.ER_ValueObjects.FundamentoInvalidoException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SolicitudEliminacion {
    private Hecho hecho;
    private String fundamento;
    @Getter
    private EstadoSolicitudEliminacion estadoSolicitudEliminacion;
    private LocalDateTime fechaCreacion;
    @Setter
    private ResolucionSolicitudEliminacion resolucionSolicitudEliminacion;
    private Contribuyente contribuyente;

    public SolicitudEliminacion(Hecho hecho, String fundamento, Contribuyente contribuyente, LocalDateTime fechaCreacion) throws FundamentoInvalidoException {
        this.hecho = hecho;
        if (fundamento.length() < 500) throw new FundamentoInvalidoException("El fundamento debe tener al menos 500 caracteres");
        this.fundamento = fundamento;
        this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.PENDIENTE;
        this.fechaCreacion = fechaCreacion;
        this.resolucionSolicitudEliminacion = null;
        this.contribuyente = contribuyente;
    }

    public void serAceptada(Administrador administrador) throws Exception {
        this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.ACEPTADA;

        try {
            this.resolucionSolicitudEliminacion = crearResolucionSolicitudEliminacion(administrador, LocalDateTime.now());
        } catch (Exception e) {
            // TODO: Corregir system out por otra excepcion que será recibida por una capa superior
            System.out.println(e.getMessage());
        }

        try {
            this.hecho.eliminar();
        }   catch (Exception e) {
            // TODO: Corregir system out por otra excepcion que será recibida por una capa superior
            System.out.println("El hecho ya fue eliminado");
        }
    }

    public void serRechazada(Administrador administrador) throws Exception {
        this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.RECHAZADA;

        try {
            this.resolucionSolicitudEliminacion = crearResolucionSolicitudEliminacion(administrador, LocalDateTime.now());
        } catch (Exception e) {
            // TODO: Corregir system out por otra excepcion que será recibida por una capa superior
            System.out.println(e.getMessage());
        }
    }

    private ResolucionSolicitudEliminacion crearResolucionSolicitudEliminacion(Administrador administrador, LocalDateTime fechaResolucion) throws Exception {
        if(this.resolucionSolicitudEliminacion != null) {
            throw new Exception("La solicitud de eliminacion ya fue resuelta con Estado: " + this.estadoSolicitudEliminacion);
        }
        ResolucionSolicitudEliminacion resolucionSolicitudEliminacion = new ResolucionSolicitudEliminacion(administrador, LocalDateTime.now());
        return resolucionSolicitudEliminacion;
    }
}