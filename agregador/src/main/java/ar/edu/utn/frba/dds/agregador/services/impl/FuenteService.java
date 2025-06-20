package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.services.IFuenteAdapter;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class FuenteService implements IFuenteService {
  @Setter
  private List<IFuenteAdapter> fuentes;


  // Constructor
  public FuenteService() {
    this.fuentes = new ArrayList<>();
  }
  public List<Hecho> buscarHechos(){
    List<Hecho> todosLosHechos = this.fuentes.stream()
        .map(IFuenteAdapter::buscarHechos)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return todosLosHechos;
  }
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    List<Hecho> nuevosHechos = fuentes
        .stream().filter(f -> f.getTipoFuente()!=TipoFuente.PROXY) // No me da los hechos de la proxy
        .map(f -> f.buscarNuevosHechos(ultimaFechaRefresco))
        .flatMap(List::stream).collect(Collectors.toList());
    return nuevosHechos;
  }
  // Eliminar de la fuente
  public void eliminarHecho(Hecho hecho){
    if(hecho.getOrigen() == Origen.CONTRIBUYENTE){
      this.fuentes.stream().filter(fuente -> fuente.getTipoFuente() == TipoFuente.DINAMICA).forEach(fuente -> fuente.eliminarHecho(hecho));
    }
    if(hecho.getOrigen() == Origen.DATASET){
      this.fuentes.stream().filter(fuente -> fuente.getTipoFuente() == TipoFuente.ESTATICA).forEach(fuente -> fuente.eliminarHecho(hecho));
    }
    if(hecho.getOrigen() == Origen.PROXY){
      this.fuentes.stream().filter(fuente -> fuente.getTipoFuente() == TipoFuente.PROXY).forEach(fuente -> fuente.eliminarHecho(hecho));
    }
  }
  public void agregarFuenteAdapter(IFuenteAdapter fuenteAdapter){
    this.fuentes.add(fuenteAdapter);
  }
  public List<Hecho> buscarHechosFuente(TipoFuente tipoFuente){
    IFuenteAdapter fuente = this.fuentes.stream().filter(f -> f.getTipoFuente().equals(tipoFuente)).findFirst().get();
    List<Hecho> hechos = fuente.buscarHechos();
    return hechos;
  }
}

