package models.entities.fuentes;
import models.entities.hechos.Hecho;

import java.util.List;

public interface Fuente {
    public List<Hecho> importarHechos();
}
