
package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.HechoFuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FuenteService implements IFuenteService {
  @Autowired
  private IFuenteRepository fuenteRepository;

  @Autowired
  private IHechoRepository hechoRepository;

  @Override
  public Fuente incorporarFuente(FuenteInputDTO fuenteInputDTO) {
      return Mono.fromCallable(() -> {
            Fuente fuente = FuenteInputDTO.DTOToFuente(fuenteInputDTO);
            fuente.setFechaCarga(LocalDateTime.now());
            Fuente fuenteAux = this.fuenteRepository.findByNombre(fuente.getNombre());
            if(fuenteAux != null){
                fuente.setId(fuenteAux.getId());
            }
            return this.fuenteRepository.save(fuente);
          })
          .subscribeOn(Schedulers.boundedElastic())
          .block();
  }

  @Override
  public void eliminarHecho(Hecho hecho) {
    Mono.fromRunnable(() -> {
          List<Fuente> fuentes = hecho
                  .getFuenteSet()
                  .stream()
                  .map(HechoFuente::getFuente)
                  .toList();
          fuentes.forEach(f -> eliminarHecho(hecho));
        })
        .subscribeOn(Schedulers.boundedElastic())
        .block(); 
  }

  @Override
  public List<FuenteOutputDTO> buscarFuentesOutput(Boolean nuevos) {
      List<Fuente> fuentes;
      if(nuevos){
          fuentes = this.fuenteRepository
                  .findByFechaCargaBetween(LocalDateTime.now().minus(Duration.ofDays(7)), LocalDateTime.now());
      }
      else {
          fuentes = this.fuenteRepository.findAll();
      }
    return fuentes.stream()
            .map(fuente -> {
                Integer cantidadHechos = (int) this.hechoRepository.countByFuenteId(fuente.getId());
                return FuenteOutputDTO.toOutputDTO(fuente, cantidadHechos);
            })
            .toList();
  }

  @Override
  public Flux<Hecho> buscarHechosFuenteStream(String nombre) {
    return Mono.fromCallable(() -> fuenteRepository.findByNombre(nombre))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMapMany(fuente -> {
          if (fuente == null) {
            return Flux.empty();
          }
          try {
            return fuente.importarHechos();
          } catch (Exception e) {
            System.err.println("Error procesando fuente " + fuente.getNombre());
            return Flux.empty();
          }
        })
        .onErrorResume(error -> {
          System.err.println("Error procesando fuente " + nombre + ": " + error.getMessage());
          return Flux.empty();
        });
  }
}
