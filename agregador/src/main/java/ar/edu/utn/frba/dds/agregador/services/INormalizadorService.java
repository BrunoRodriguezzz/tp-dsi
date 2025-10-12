package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import reactor.core.publisher.Flux;

public interface INormalizadorService {
    public Hecho normalizarHecho(Hecho hecho);
    public Flux<Hecho> normalizarHechosReactivo(Flux<Hecho> hechos);
}
