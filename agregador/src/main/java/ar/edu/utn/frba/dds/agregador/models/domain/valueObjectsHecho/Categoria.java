package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho;

import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.CategoriaInvalidaException;
import lombok.Getter;
import lombok.Setter;

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
