package entities;

import java.time.LocalDate;

public class Etiqueta implements ElementoCriterio<Etiqueta> {

    @Override
    public Boolean coincide(Etiqueta etiqueta) throws Exception {
        // TODO
        /*if(!(fecha instanceof LocalDate)){
            throw new Exception("Fecha Inválida");
        }*/
        return true;
    }
}
