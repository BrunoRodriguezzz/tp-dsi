package entities;

import java.time.LocalDate;

public class Etiqueta implements ElementoCriterio<Etiqueta> {
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
