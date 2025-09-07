package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuenteService implements IFuenteService {
  @Autowired
  private IFuenteRepository fuenteRepository;

  public List<Hecho> buscarHechos() {
    List<Fuente> fuentes = this.fuenteRepository.findAll();
    List<Hecho> todosLosHechos = fuentes.stream()
        .map(Fuente::importarHechos)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return todosLosHechos;
  }

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    List<Fuente> fuentes = this.fuenteRepository.findAll();
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

  @Override
  public List<FuenteOutputDTO> buscarFuentesOutput() {
    return this.fuenteRepository
            .findAll()
            .stream()
            .map(FuenteOutputDTO::toOutputDTO)
            .collect(Collectors.toList());
  }

  public List<Hecho> buscarHechosFuente(TipoFuente tipoFuente){
    List<Fuente> fuentes = this.fuenteRepository.findAll();
    List<Fuente> fuentesFiltradas = fuentes.stream().filter(f-> f.getTipoFuente().equals(tipoFuente)).toList();
    List<Hecho> hechos = fuentesFiltradas.stream()
        .map(Fuente::importarHechos)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return hechos;
  }

  @Override
  public List<Hecho> buscarHechosFuente(String nombre) {
    Fuente fuente = this.fuenteRepository.findByNombre(nombre);
    List<Hecho> hechosFuente = fuente.importarHechos();
    return hechosFuente;
  }

  @Override
  public Fuente buscarFuente(Long id) {
    return this.buscarPorID(id);
  }

  @Override
  public Fuente buscarFuente(String nombre) {
    return this.fuenteRepository.findByNombre(nombre);
  }

  @Override
  public List<Fuente> buscarFuentes() {
    return this.fuenteRepository.findAll();
  }

  @Override
  public Fuente incorporarFuente(FuenteInputDTO fuenteInputDTO) {
    Fuente fuente = FuenteInputDTO.DTOToFuente(fuenteInputDTO);
    Fuente existente = this.fuenteRepository.findByIdInternoFuenteAndTipoFuente(fuente.getIdInternoFuente(), fuente.getTipoFuente());
    if (existente != null) {
      return existente;
    }
    return this.fuenteRepository.save(fuente);
  }

  private Fuente buscarPorID(Long id){
    Optional<Fuente> fuenteOptional = this.fuenteRepository.findById(id);
    if(fuenteOptional.isPresent()){
      return fuenteOptional.get();
    }
    else {
      throw new NotFoundException("Fuente no encontrada.");
    }
  }
}

