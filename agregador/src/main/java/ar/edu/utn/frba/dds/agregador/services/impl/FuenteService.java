
package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import jakarta.persistence.EntityNotFoundException;
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

  @Override
  public List<Hecho> buscarHechos() {
    return buscarHechosStream()
        .collectList()
        .block();
  }

  @Override
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    return buscarNuevosHechosStream(ultimaFechaRefresco)
        .collectList()
        .block(); 
  }

  @Override
  public List<Hecho> buscarHechosFuente(TipoFuente tipoFuente) {
    return buscarHechosFuenteStream(tipoFuente)
        .collectList()
        .block();
  }

  @Override
  public List<Hecho> buscarHechosFuente(String nombre) {
    return buscarHechosFuenteStream(nombre)
        .collectList()
        .block();
  }

    @Override
    public List<Hecho> buscarHechosProxy() {

        return this.fuenteRepository.findByTipoFuente(TipoFuente.PROXY)
                .stream()
                .map(f -> f.importarHechos().blockLast())
                .toList();
    }

    @Override
  public Fuente buscarFuente(String nombre) {
    return Mono.fromCallable(() -> fuenteRepository.findByNombre(nombre))
        .subscribeOn(Schedulers.boundedElastic())
        .block(); 
  }

  @Override
  public Fuente findById(Long id) {
    return this.fuenteRepository.findById(id).orElse(null);
  }

  @Override
  public List<Fuente> buscarFuentes() {
    return Mono.fromCallable(() -> fuenteRepository.findAll())
        .subscribeOn(Schedulers.boundedElastic())
        .block(); 
  }

  @Override
  public Fuente incorporarFuente(FuenteInputDTO fuenteInputDTO) {
    Fuente fuenteIncorporada = Mono.fromCallable(() -> {
          Fuente fuente = FuenteInputDTO.DTOToFuente(fuenteInputDTO);
          Fuente fuenteAux = this.fuenteRepository.findByNombre(fuente.getNombre());
          if(fuenteAux != null){
              fuente.setId(fuenteAux.getId());
          }
          return this.fuenteRepository.save(fuente);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .block();
    return fuenteIncorporada;
  }

  @Override
  public void eliminarHecho(Hecho hecho) {
    Mono.fromRunnable(() -> {
          Fuente fuente = hecho.getFuente();
          fuente.eliminarHecho(hecho);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .block(); 
  }

  @Override
  public List<FuenteOutputDTO> buscarFuentesOutput() {
    return Mono.fromCallable(() -> fuenteRepository.findAll())
        .flatMapMany(Flux::fromIterable)
        .map(FuenteOutputDTO::toOutputDTO)
        .collectList()
        .block(); 
  }

  @Override
  public Flux<Hecho> buscarHechosStream() {
    return Mono.fromCallable(() -> fuenteRepository.findAll())
        .subscribeOn(Schedulers.boundedElastic())
        .flatMapMany(Flux::fromIterable)
        .flatMap(fuente -> {
          try {
            return fuente.importarHechos();
          } catch (Exception e) {
            System.err.println("Error procesando fuente " + fuente.getNombre());
            return Flux.empty();
          }
        })
        .onErrorContinue((error, fuente) -> {
          System.err.println("Error procesando fuente: " + error.getMessage());
        });
  }

  @Override
  public Flux<Hecho> buscarNuevosHechosStream(LocalDateTime ultimaFechaRefresco) {
    return Mono.fromCallable(() -> fuenteRepository.findAll())
        .subscribeOn(Schedulers.boundedElastic())
        .flatMapMany(Flux::fromIterable)
        .flatMap(fuente -> {
          try {
            return fuente.buscarNuevosHechos(ultimaFechaRefresco);
          } catch (Exception e) {
            System.err.println("Error procesando fuente " + fuente.getNombre());
            return Flux.empty();
          }
        })
        .onErrorContinue((error, fuente) -> {
          System.err.println("Error buscando nuevos hechos en fuente: " + error.getMessage());
        });
  }

  @Override
  public Flux<Hecho> buscarHechosFuenteStream(TipoFuente tipoFuente) {
    return Mono.fromCallable(() -> fuenteRepository.findByTipoFuente(tipoFuente))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMapMany(Flux::fromIterable)
        .flatMap(fuente -> {
          try {
            return fuente.importarHechos();
          } catch (Exception e) {
            System.err.println("Error procesando fuente " + fuente.getNombre());
            return Flux.empty();
          }
        })
        .onErrorContinue((error, fuente) -> {
          System.err.println("Error procesando fuente tipo " + tipoFuente + ": " + error.getMessage());
        });
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
  
  private Fuente buscarPorID(Long id) {
    return this.fuenteRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No se encontró la fuente con ID: " + id));
  }
  
//  public static class FuenteHechosResult {
//    private String nombreFuente;
//    private List<Hecho> hechos;
//    private String error;
//    private LocalDateTime timestamp;
//
//    public FuenteHechosResult(String nombreFuente, List<Hecho> hechos) {
//      this.nombreFuente = nombreFuente;
//      this.hechos = hechos;
//      this.timestamp = LocalDateTime.now();
//    }
//
//    public FuenteHechosResult(String nombreFuente, List<Hecho> hechos, String error) {
//      this(nombreFuente, hechos);
//      this.error = error;
//    }
//
//    // Getters
//    public String getNombreFuente() { return nombreFuente; }
//    public List<Hecho> getHechos() { return hechos; }
//    public String getError() { return error; }
//    public LocalDateTime getTimestamp() { return timestamp; }
//    public boolean tieneError() { return error != null; }
//    public int getCantidadHechos() { return hechos.size(); }
//  }
}
