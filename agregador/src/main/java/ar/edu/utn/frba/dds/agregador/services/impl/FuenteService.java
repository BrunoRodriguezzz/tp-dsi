package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuenteService implements IFuenteService {
  @Autowired
  private IFuenteRepository fuenteRepository;

  public List<Hecho> buscarHechos() {
    List<Fuente> fuentes = this.fuenteRepository.buscarFuentes();
    List<Hecho> todosLosHechos = fuentes.stream()
        .map(Fuente::importarHechos)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return todosLosHechos;
  }

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    List<Fuente> fuentes = this.fuenteRepository.buscarFuentes();
    List<Hecho> nuevosHechos = fuentes
        .stream()
        .map(f -> f.buscarNuevosHechos(ultimaFechaRefresco))
        .flatMap(List::stream).collect(Collectors.toList());
    return nuevosHechos;
  }

  // Eliminar de la fuente
  public void eliminarHecho(Hecho hecho){
    hecho.getFuente().eliminarHecho(hecho);
  }

  public List<Hecho> buscarHechosFuente(TipoFuente tipoFuente){
    List<Fuente> fuentes = this.fuenteRepository.buscarFuentes();
    List<Fuente> fuentesFiltradas = fuentes.stream().filter(f-> f.getTipoFuente().equals(tipoFuente)).toList();
    List<Hecho> hechos = fuentesFiltradas.stream()
        .map(Fuente::importarHechos)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return hechos;
  }

  @Override
  public Fuente buscarFuente(Long id) {
    return this.fuenteRepository.buscarFuente(id);
  }

  @Override
  public Fuente buscarFuente(String nombre) {
    return this.fuenteRepository.buscarFuente(nombre);
  }

  @Override
  public Fuente incorporarFuente(FuenteInputDTO fuenteInputDTO) {
    Fuente fuente = FuenteInputDTO.DTOToFuente(fuenteInputDTO);
    return this.fuenteRepository.guardarFuente(fuente);
  }
}

