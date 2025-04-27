package models.entities.solicitudEliminacion;

import lombok.Getter;
import lombok.Setter;
import models.entities.usuarios.Administrador;

import java.time.LocalDateTime;

public class ResolucionSolicitudEliminacion {
    @Getter@Setter
    private Administrador administrador;
    private LocalDateTime fechaResolucion;

    public ResolucionSolicitudEliminacion(Administrador administrador, LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
        this.administrador = administrador;
    }

}
