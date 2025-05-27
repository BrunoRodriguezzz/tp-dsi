package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import java.util.List;

public interface TipoArchivo {
    List<HechoEstatica> importarHechos(String ruta);
}
