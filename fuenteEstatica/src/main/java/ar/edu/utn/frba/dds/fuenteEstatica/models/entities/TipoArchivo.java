package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import reactor.core.publisher.Flux;

public interface TipoArchivo {
    Flux<HechoEstatica> importarHechos(String ruta);
}
