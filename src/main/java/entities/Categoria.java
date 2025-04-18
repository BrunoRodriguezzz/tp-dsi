package entities;

import java.time.LocalDate;

public class Categoria implements ElementoCriterio<Categoria> {
    private String titulo;

    public Categoria(String titulo) {
        this.titulo = titulo;
    }

    @Override
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
