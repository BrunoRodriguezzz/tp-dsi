package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.services.impl.TipoFuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.List;
import reactor.core.publisher.Mono;

public interface IFuenteAdapter {
  public List<Hecho> buscarHechos();
  List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco);
  public TipoFuente getTipoFuente();
  public void eliminarHecho(Hecho hecho);
}
