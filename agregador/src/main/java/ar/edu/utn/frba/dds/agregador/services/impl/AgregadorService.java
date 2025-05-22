package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AgregadorService implements IAgregadorService {
  private IFuenteService fuenteEstatica;
  private IFuenteService fuenteDinamica;
  private IFuenteService fuenteProxy;

  // Constructor
  public AgregadorService(
      IFuenteService fuenteEstatica,
      IFuenteService fuenteDinamica,
      IFuenteService fuenteProxy
  ) {
    this.fuenteEstatica = fuenteEstatica;
    this.fuenteDinamica = fuenteDinamica;
    this.fuenteProxy = fuenteProxy;
  }

  @Override
  public List<Hecho> buscarHechos(){
    List<Hecho> hechosEstatica = this.fuenteEstatica.buscarHechos();
    List<Hecho> hechosDinamica = this.fuenteDinamica.buscarHechos();
    List<Hecho> hechosProxy = this.fuenteProxy.buscarHechos();

    List<Hecho> hechos = new ArrayList<>();

    hechos.addAll(hechosEstatica);
    hechos.addAll(hechosDinamica);
    hechos.addAll(hechosProxy);

    return hechos;
  }
}
