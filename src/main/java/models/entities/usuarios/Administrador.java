package models.entities.usuarios;

import models.entities.solicitudEliminacion.SolicitudEliminacion;

public class Administrador {
    private String nombre;
    private String apellido;

    public Administrador(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }
}
