package ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion;

import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Administrador;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
public class ResolucionSolicitudEliminacion {
    private Administrador administrador;
    private LocalDateTime fechaResolucion;

    public ResolucionSolicitudEliminacion(Administrador administrador, LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
        this.administrador = administrador;
    }

}
