package ar.edu.utn.frba.dds.domain.models.entities.fuentes;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class Fuente {
  private WebClient fuenteAPI;
  public abstract List<Hecho> importarHechos();
  public abstract List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco);
  public abstract void eliminarHecho(Hecho hecho);
}