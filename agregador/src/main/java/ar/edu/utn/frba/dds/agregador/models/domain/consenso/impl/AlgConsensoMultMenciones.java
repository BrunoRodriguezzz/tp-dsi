package ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consensuador;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.IStratConsenso;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

public class AlgConsensoMultMenciones implements IStratConsenso {
  @Getter
  private static final AlgConsensoMultMenciones instance = new AlgConsensoMultMenciones();
  private AlgConsensoMultMenciones() {}

  public Hecho consensuados(List<Hecho> hechos, List<Fuente> fuentes, Hecho hecho) {
    List<Long> idFuentes = hechos.stream()
        .map(h -> h.getFuentes().stream().map(Fuente::getId).toList())
        .distinct()
        .toList()
        .stream().flatMap(List::stream).toList();

    if(idFuentes.size() >= 2 && comprobarAtributos(hechos, hecho)) {
      hecho.agregarConsenso(Consenso.MULTMENCIONES);
    }

    return hecho;
  }

  private static boolean comprobarAtributos(List<Hecho> hechos, Hecho hecho) {
    return hechos.stream().allMatch(hecho::equals);
  }
}
// si al menos dos fuentes contienen un mismo hecho y ninguna otra fuente contiene otro de
// igual título pero diferentes atributos, se lo considera consensuado;