package models.entities.filtros;

import lombok.Getter;
import models.entities.criterios.ElementoCriterio;
@Getter
public class Etiqueta {
    private String titulo;

    public Etiqueta(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Boolean coincide(Etiqueta etiqueta) throws RuntimeException {
        if(!(etiqueta instanceof Etiqueta)){
            throw new RuntimeException("Etiqueta Invalida");
        }
        return this.titulo.equals(etiqueta.titulo);
    }
}
