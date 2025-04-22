package models.entities.criterios;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Categoria {
    @Setter
    private String titulo;

    public Categoria(String titulo) {
        this.titulo = titulo;
    }

    public Boolean coincide(Categoria categoria){
        if(categoria.titulo == null) {
            throw new RuntimeException("el titulo de la Categoria no puede estar vacio");
        }
        return this.titulo.equals(categoria.titulo);
    }
}
