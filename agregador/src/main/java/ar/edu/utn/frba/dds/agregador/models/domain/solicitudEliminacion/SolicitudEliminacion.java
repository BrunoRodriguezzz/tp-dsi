package ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.FundamentoInvalidoException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.HechoYaEliminadoException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.SolicitudEliminacionYaResueltaException;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter @Getter
public class SolicitudEliminacion {
    private Long id;
    private Hecho hecho;
    private String fundamento;
    private EstadoSolicitudEliminacion estadoSolicitudEliminacion;
    private LocalDateTime fechaCreacion;
    private ResolucionSolicitudEliminacion resolucionSolicitudEliminacion;
    private Contribuyente contribuyente;

    public SolicitudEliminacion(Hecho hecho, String fundamento, Contribuyente contribuyente, LocalDateTime fechaCreacion) throws FundamentoInvalidoException {
        this.hecho = hecho;
        if (fundamento.length() < 500) throw new FundamentoInvalidoException("El fundamento debe tener al menos 500 caracteres");
        this.fundamento = fundamento;
        this.fechaCreacion = fechaCreacion;
        this.resolucionSolicitudEliminacion = null;
        this.contribuyente = contribuyente;
        this.id = null;
    }

    public void serAceptada(Administrador administrador) throws Exception {
        try {
            if(this.estadoSolicitudEliminacion != EstadoSolicitudEliminacion.ACEPTADA || this.estadoSolicitudEliminacion != EstadoSolicitudEliminacion.RECHAZADA){
                this.resolucionSolicitudEliminacion = crearResolucionSolicitudEliminacion(administrador);
                this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.ACEPTADA;
                this.hecho.eliminar();
            }
        } catch (SolicitudEliminacionYaResueltaException e) {
            // TODO: Corregir system out por otra excepcion que será recibida por una capa superior
            System.out.println(e.getMessage());
        } catch (HechoYaEliminadoException e) {
            // TODO: Corregir system out por otra excepcion que será recibida por una capa superior
            System.out.println("El hecho ya fue eliminado");
        }
    }

    public void serRechazada(Administrador administrador) throws Exception {
        try {
            if(this.estadoSolicitudEliminacion != EstadoSolicitudEliminacion.ACEPTADA || this.estadoSolicitudEliminacion != EstadoSolicitudEliminacion.RECHAZADA){
                this.resolucionSolicitudEliminacion = crearResolucionSolicitudEliminacion(administrador);
                this.estadoSolicitudEliminacion = EstadoSolicitudEliminacion.RECHAZADA;
            }
        } catch (SolicitudEliminacionYaResueltaException e) {
            // TODO: Corregir system out por otra excepcion que será recibida por una capa superior
            System.out.println(e.getMessage());
        }
    }

    private ResolucionSolicitudEliminacion crearResolucionSolicitudEliminacion(Administrador administrador) throws Exception {
        if(this.resolucionSolicitudEliminacion != null) {
            throw new Exception("La solicitud de eliminacion ya fue resuelta con Estado: " + this.estadoSolicitudEliminacion);
        }
        ResolucionSolicitudEliminacion resolucionSolicitudEliminacion = new ResolucionSolicitudEliminacion(administrador, LocalDateTime.now());
        return resolucionSolicitudEliminacion;
    }
}