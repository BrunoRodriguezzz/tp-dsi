package ar.edu.utn.frba.dds.domain.models.entities.usuarios;

import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;

public class Administrador {
    private String nombre;
    private String apellido;

    public Administrador(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }
}
