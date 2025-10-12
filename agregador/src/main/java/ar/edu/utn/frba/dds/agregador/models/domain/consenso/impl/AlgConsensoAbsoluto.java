package ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.IStratConsenso;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;
import lombok.Getter;

public class AlgConsensoAbsoluto implements IStratConsenso {
  @Getter
  private static final AlgConsensoAbsoluto instance = new AlgConsensoAbsoluto();
  private AlgConsensoAbsoluto() {}

  public Hecho consensuados(List<Hecho> hechos, List<Fuente> fuentes, Hecho hecho) {
    Integer cantFuentes = fuentes.size();

    Integer cantFuentesConHecho = hecho.getFuenteSet()
            .size();

    if(cantFuentesConHecho.equals(cantFuentes)) {
      hecho.agregarConsenso(Consenso.ABSOLUTO);
    }

    return hecho;
  }
}
