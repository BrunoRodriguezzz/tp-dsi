package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContenidoMultimedia {
    private String nombre;
    private String ruta;
    private Formato formato;

    public ContenidoMultimedia(String ruta) {
        this.ruta = ruta;
    }
}
