package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.services.impl.TipoFuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.List;

public interface IFuenteService {
  public List<Hecho> buscarHechos();
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco);
  public List<Hecho> buscarHechosFuente(TipoFuente tipoFuente);
  public void eliminarHecho(Hecho hecho);
}
