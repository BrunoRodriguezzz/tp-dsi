package ar.edu.utn.frba.dds.agregador.models.domain.consenso;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;

public interface IStratConsenso {
  public List<Hecho> consensuados(List<Hecho> hechos, List<Fuente> fuentes, Hecho hecho);
}
