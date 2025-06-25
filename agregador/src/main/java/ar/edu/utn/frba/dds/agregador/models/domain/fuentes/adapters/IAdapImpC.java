package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public interface IAdapImpC {
  public List<Coleccion> importarColecciones(WebClient webClient, Long idInternoFuente);
  public Coleccion importarColeccion(WebClient webClient, Long id, Long idInternoFuente);
  public List<Hecho> importarHechosColeccion(WebClient webClient, Long id, Long idInternoFuente);
}
