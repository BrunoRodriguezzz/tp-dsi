package ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho;

import lombok.Getter;
import lombok.Setter;
import models.entities.utils.Errores.ER_ValueObjects.CategoriaInvalidaException;

@Getter
public class Categoria {
    @Setter
    private String titulo;

    public Categoria(String titulo) throws CategoriaInvalidaException {
        if(titulo == null || titulo.isBlank())
            throw new CategoriaInvalidaException("La Categoria no puede tener un titulo vacío");
        this.titulo = titulo;
    }
}
