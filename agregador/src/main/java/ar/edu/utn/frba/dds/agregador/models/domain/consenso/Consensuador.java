package ar.edu.utn.frba.dds.agregador.models.domain.consenso;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl.AlgConsensoAbsoluto;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl.AlgConsensoMaySimple;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl.AlgConsensoMultMenciones;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Consensuador {
  private List<IStratConsenso> stratConsensos;

  // Clase Singleton
  @Getter // Método público para obtener las instancia
  private static final Consensuador instance = new Consensuador(); // Creacion de la instancia clase única
  private Consensuador() {
    this.stratConsensos = new ArrayList<>();
    this.stratConsensos.add(AlgConsensoMultMenciones.getInstance());
    this.stratConsensos.add(AlgConsensoMaySimple.getInstance());
    this.stratConsensos.add(AlgConsensoAbsoluto.getInstance());
  } // Constructor privado para evitar instanciación externa

  public List<Hecho> consensuarHechos(List<Fuente> fuentes, List<Hecho> hechos) {
    List<Hecho> hechosConsensuados = new ArrayList<>();

    // Consensuar basándose en los hechos ya guardados en BD (no consultar fuentes externas)
    hechos.forEach(h -> {
      // Buscar hechos con el mismo título dentro de los hechos guardados
      List<Hecho> hechosMismoNombre = hechos.stream()
          .filter(otroHecho -> otroHecho.getTitulo() != null &&
                               otroHecho.getTitulo().equalsIgnoreCase(h.getTitulo()))
          .collect(Collectors.toList());

      int consensosAntes = h.getConsensos() != null ? h.getConsensos().size() : 0;

      this.stratConsensos.forEach(strat -> {
        strat.consensuados(hechosMismoNombre, fuentes, h);
      });

      int consensosDespues = h.getConsensos() != null ? h.getConsensos().size() : 0;

      hechosConsensuados.add(h);
    });

    long totalConConsenso = hechosConsensuados.stream()
            .filter(h -> h.getConsensos() != null && !h.getConsensos().isEmpty())
            .count();

    return hechosConsensuados;
  }

  // Método antiguo que consulta fuentes externas (muy lento, dejarlo para casos específicos)
  private List<Hecho> pedirHechosRepetidos(List<Fuente> fuentes, Hecho hecho) {
    return Flux.fromIterable(fuentes)
        .flatMap(fuente -> fuente.importarHechosMismoTitulo(hecho)) // Flux<Hecho>
        .collectList() // Mono<List<Hecho>>
        .block(); // List<Hecho>
  }

}
