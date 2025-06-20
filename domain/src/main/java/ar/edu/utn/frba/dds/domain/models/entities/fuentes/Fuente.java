package ar.edu.utn.frba.dds.domain.models.entities.fuentes;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;

import java.util.List;

public interface Fuente {
    public List<Hecho> importarHechos();
}
