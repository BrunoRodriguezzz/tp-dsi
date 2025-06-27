package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;

public interface IHechoRepository {
  public Hecho guardarHecho(Hecho hecho);
  public Boolean inicializarHechos(List<Hecho> hechos);
  public Hecho buscarHecho(Long id);
  public List<Hecho> guardarHechos(List<Hecho> hechos);
  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente);
}
