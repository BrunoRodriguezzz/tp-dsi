package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.domain.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.domain.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.models.entities.fuentes.FuenteProxy;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.domain.models.entities.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class FuenteService implements IFuenteService {
  @Setter
  private FuenteEstatica fuenteEstatica;
  private FuenteDinamica fuenteDinamica;
  private FuenteProxy fuenteProxy;

  // Constructor
  public FuenteService() {
    this.fuenteEstatica = new FuenteEstatica("http://localhost:8084");
    this.fuenteDinamica = new FuenteDinamica("http://localhost:8081");
    this.fuenteProxy = new FuenteProxy("http://localhost:8083");
  }

  public List<Hecho> buscarHechos() {
    List<Fuente> fuentes = new ArrayList<Fuente>();
    fuentes.add(this.fuenteEstatica);
    fuentes.add(this.fuenteDinamica);
    fuentes.add(this.fuenteProxy);

    List<Hecho> todosLosHechos = fuentes.stream()
        .map(Fuente::importarHechos)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return todosLosHechos;
  }
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    List<Fuente> fuentes = new ArrayList<Fuente>();
    fuentes.add(fuenteEstatica);
    fuentes.add(fuenteDinamica);

    List<Hecho> nuevosHechos = fuentes
        .stream()
        .map(f -> f.buscarNuevosHechos(ultimaFechaRefresco))
        .flatMap(List::stream).collect(Collectors.toList());
    return nuevosHechos;
  }
  // Eliminar de la fuente
  public void eliminarHecho(Hecho hecho){
    if(hecho.getOrigen() == Origen.CONTRIBUYENTE){
      this.fuenteDinamica.eliminarHecho(hecho);
    }
    if(hecho.getOrigen() == Origen.DATASET){
      this.fuenteEstatica.eliminarHecho(hecho);
    }
    if(hecho.getOrigen() == Origen.PROXY){
      this.fuenteProxy.eliminarHecho(hecho);
    }
  }

  public List<Hecho> buscarHechosFuente(TipoFuente tipoFuente){
    List<Hecho> hechos = new ArrayList<>();
    if(TipoFuente.DINAMICA.equals(tipoFuente)){
      hechos = this.fuenteDinamica.importarHechos();
    }
    if(TipoFuente.ESTATICA.equals(tipoFuente)){
      hechos = this.fuenteEstatica.importarHechos();
    }
    if(TipoFuente.PROXY.equals(tipoFuente)){
      hechos = this.fuenteProxy.importarHechos();
    }
    return hechos;
  }
}

