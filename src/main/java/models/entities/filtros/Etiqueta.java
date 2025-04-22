package models.entities.filtros;

import lombok.Getter;

@Getter
public class Etiqueta {
    private String titulo;

    public Etiqueta(String titulo) {
        this.titulo = titulo;
    }
}
