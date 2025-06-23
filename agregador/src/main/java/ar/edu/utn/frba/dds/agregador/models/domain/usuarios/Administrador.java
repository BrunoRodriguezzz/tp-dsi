package ar.edu.utn.frba.dds.agregador.models.domain.usuarios;

import lombok.Getter;
import lombok.Setter;

public class Administrador {
    @Getter @Setter
    private Long id;
    @Getter
    private String nombre;
    @Getter
    private String apellido;

    public Administrador(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }
}
