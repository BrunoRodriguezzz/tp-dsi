package models.entities.solicitudEliminacion;

import lombok.Getter;
import lombok.Setter;
import models.entities.usuarios.Contribuyente;

import java.time.LocalDateTime;

public class CambioDeEstadoSolicitud {
    private SolicitudEliminacion solicitudEliminacion;
    @Getter@Setter
    private EstadoSolicitudEliminacion estadoSolicitudEliminacion;
    private LocalDateTime fechaCambioEstadoSolicitud;

    public CambioDeEstadoSolicitud(SolicitudEliminacion solicitudEliminacion, EstadoSolicitudEliminacion estadoSolicitudEliminacion) {
        this.solicitudEliminacion = solicitudEliminacion;
        this.estadoSolicitudEliminacion = estadoSolicitudEliminacion;
        this.fechaCambioEstadoSolicitud = LocalDateTime.now();
    }

}
