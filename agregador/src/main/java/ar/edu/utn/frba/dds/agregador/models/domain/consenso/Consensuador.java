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

    // Fuente envía los que tengan el mismo titulo
    hechos.forEach(h -> {
      List<Hecho> hechosMismoNombre = this.pedirHechosRepetidos(fuentes, h);
      this.stratConsensos.forEach(strat -> {
        strat.consensuados(hechosMismoNombre, fuentes, h);
      });
      hechosConsensuados.add(h);
    });

    return hechosConsensuados;
  }

  private List<Hecho> pedirHechosRepetidos (List<Fuente> fuentes, Hecho hecho) {
    List<Hecho> hechosRepetidos = fuentes
        .stream().map(f -> {
          return f.importarHechosMismoTitulo(hecho);
        })
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return hechosRepetidos;
  }
}
