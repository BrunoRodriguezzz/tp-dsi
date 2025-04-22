package models.entities.filtros;

import lombok.Getter;

@Getter
public class Categoria {
    private String titulo;

    public Categoria(String titulo) {
        this.titulo = titulo;
    }

    public Boolean coincide(Categoria categoria) throws RuntimeException {
        if(!(categoria instanceof Categoria)){
            throw new RuntimeException("Categoria Invalida");
        }
        return this.titulo.equals(categoria.titulo);
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
