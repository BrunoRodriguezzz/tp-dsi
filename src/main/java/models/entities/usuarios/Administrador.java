package models.entities.usuarios;

import models.entities.solicitudEliminacion.SolicitudEliminacion;

public class Administrador {
    private String nombre;
    private String apellido;

    public Administrador(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Boolean aceptarSolicitud(SolicitudEliminacion solicitudEliminacion) {
        solicitudEliminacion.serAceptada(this);
        return true;
    }

    public Boolean rechazarSolicitudSolicitud(SolicitudEliminacion solicitudEliminacion) {
        solicitudEliminacion.serRechazada(this);
        return true;
    }
}
