package ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.IStratConsenso;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;
import lombok.Getter;

public class AlgConsensoMaySimple implements IStratConsenso {
  @Getter
  private static final AlgConsensoMaySimple instance = new AlgConsensoMaySimple();
  private AlgConsensoMaySimple() {}

  public Hecho consensuados(List<Hecho> hechos, List<Fuente> fuentes, Hecho hecho) {
    int cantFuentes = fuentes.size();

    int cantFuentesConHecho = hechos.stream()
        .filter(h -> h.equals(hecho))
        .map(h -> h.getFuente().getId())
        .distinct()
        .toList()
        .size();

    if(cantFuentesConHecho*2 >= cantFuentes) {
      hecho.agregarConsenso(Consenso.MAYSIMPLE);
    }

    return hecho;
  }
}
// mayoría simple: si al menos la mitad de las fuentes contienen el mismo hecho, se lo considera consensuado;
