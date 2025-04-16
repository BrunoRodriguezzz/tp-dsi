package entities;

import java.time.LocalDate;

public class Categoria implements ElementoCriterio<Categoria> {
    private String titulo;

    @Override
    public Boolean coincide(Categoria categoria) throws Exception {
        if(!(categoria instanceof Categoria)){
            throw new Exception("Categoria Invalida");
        }
        return this.titulo.equals(categoria.titulo);
    }

}
