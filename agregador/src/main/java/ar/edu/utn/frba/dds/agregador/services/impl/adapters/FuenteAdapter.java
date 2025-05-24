package ar.edu.utn.frba.dds.agregador.services.impl.adapters;

import ar.edu.utn.frba.dds.agregador.services.IFuenteAdapter;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class FuenteAdapter implements IFuenteAdapter {
  WebClient fuenteAPI;

  public FuenteAdapter(WebClient fuenteAPI) {
    this.fuenteAPI = fuenteAPI;
  }

  public Mono<Hecho> buscarHechos() {
    return null;
  }

  public Mono<Hecho> buscarNuevosHechos() {
    return null;
  }
}
