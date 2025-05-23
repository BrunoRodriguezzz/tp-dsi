package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;

public interface IHechoRepository {
  public Hecho guardarHecho(Hecho hecho);
  public Boolean inicializarHechos(List<Hecho> hechos);
  public Hecho buscarHecho(Long id);
}
