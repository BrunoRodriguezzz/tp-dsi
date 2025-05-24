package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import reactor.core.publisher.Mono;

public interface IFuenteAdapter {
  Mono<Hecho> buscarHechos();
  Mono<Hecho> buscarNuevosHechos();
}
