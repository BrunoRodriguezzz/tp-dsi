package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFuenteService {
  public List<Hecho> buscarHechos();
  public List<Hecho> nuevosHechos();
}
