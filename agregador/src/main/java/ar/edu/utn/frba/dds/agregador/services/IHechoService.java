package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import java.util.List;

public interface IHechoService {
  public Hecho guardarHecho(Hecho hecho);
  public Hecho buscarHecho(Long id);
  public List<Hecho> guardarHechos(List<Hecho> hechos);
}
