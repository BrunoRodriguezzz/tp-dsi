package ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.IStratConsenso;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgConsensoMultMenciones implements IStratConsenso {
  @Getter
  private static final AlgConsensoMultMenciones instance = new AlgConsensoMultMenciones();
  private AlgConsensoMultMenciones() {}

  public Hecho consensuados(List<Hecho> hechos, List<Fuente> fuentes, Hecho hecho) {
    int cantFuentesConHecho = hecho.getFuenteSet().size();

    // Si al menos 2 fuentes mencionan el hecho
    if (cantFuentesConHecho >= 2) {
      // Verificar que no haya conflictos: ningún otro hecho con el mismo título pero atributos diferentes
      boolean hayConflictos = hechos.stream()
          .filter(h -> !h.equals(hecho))
          .filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()))
          .anyMatch(h -> !atributosIguales(h, hecho));

      // Solo consensuar si NO hay conflictos
      if (!hayConflictos) {
        hecho.agregarConsenso(Consenso.MULTMENCIONES);
      }
    }

    return hecho;
  }

  private static boolean atributosIguales(Hecho h1, Hecho h2) {
    return h1.equals(h2);
  }
}
// si al menos dos fuentes contienen un mismo hecho y ninguna otra fuente contiene otro de
// igual título pero diferentes atributos, se lo considera consensuado;