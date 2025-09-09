
package ar.edu.utn.frba.dds.agregador.models.domain.fuentes;

import ar.edu.utn.frba.dds.agregador.converters.TipoFuenteConverter;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapUbicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.*;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpC;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Pais;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fuente")
public class Fuente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "fuente_interno_id")
  private Long idInternoFuente;

  @Column(nullable = false)
  private String nombre;

  @Column(nullable = false)
  private String path;

  @Transient
  private WebClient webClient;

  @Column(nullable = false)
  @Convert(converter = TipoFuenteConverter.class)
  private TipoFuente tipoFuente;

  // Adaptadores originales (para compatibilidad)
  @Transient
  private IAdapImpH iAdapImpH;

  @Transient
  private IAdapImpC iAdapImpC;

  public Fuente(String nombre, String path, TipoFuente tipoFuente, Long idInternoFuente) {
    this.nombre = nombre;
    this.path = path;
    this.tipoFuente = tipoFuente;
    this.idInternoFuente = idInternoFuente;
    this.inicializar();
  }


  public Flux<Hecho> importarHechos() {
      log.info("Importando hechos de la fuente: {}", this.nombre);
    return iAdapImpH.importarHechos(this.webClient, this)
        .flatMap(this::verificarHecho)
        .onErrorResume(error -> {
          System.err.println("Error en importarHechos de fuente " + this.nombre + ": " + error.getMessage());
          return Flux.empty();
        });
  }

  public Flux<Hecho> importarHechosMismoTitulo(Hecho hecho) {
    return iAdapImpH.importarHechosMismoTitulo(this.webClient, this, hecho)
        .flatMap(this::verificarHecho);
  }

  public Flux<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    return iAdapImpH.buscarNuevosHechos(ultimaFechaRefresco, this.webClient, this)
        .flatMap(this::verificarHecho);
  }

  public Mono<Void> eliminarHecho(Hecho hecho) {
    return this.iAdapImpH.eliminarHecho(hecho, webClient, this);
  }

  @PostLoad
  private void inicializar() {
    switch(this.tipoFuente) {
      case DINAMICA -> {
        this.webClient = WebClient.builder()
            .baseUrl(path + "/api/fuenteDinamica")
            .build();
        this.iAdapImpH = AdapImpHDinamico.getInstance();
      }
      case ESTATICA -> {
        this.webClient = WebClient.builder()
            .baseUrl(path)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
            .build();
        this.iAdapImpH = AdapImpH.getInstance();
      }
      case PROXY -> {
        this.webClient = WebClient.builder()
            .baseUrl(path)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
            .build();
        this.iAdapImpH = AdapImpH.getInstance();
        this.iAdapImpC = AdapImpC.getInstance();
      }
    }
  }

  private Mono<Hecho> verificarHecho(Hecho h) {
    h.getUbicacion().setPais(Pais.ARGENTINA);
    return Mono.fromCallable(() -> {
          h.setFuente(this);
          return h;
        })
        .flatMap(this::cargarUbicacionReactiva)
        .subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<Hecho> cargarUbicacionReactiva(Hecho hecho) {
      log.info("Cargando ubicacion para el hecho: {}", hecho.getTitulo());
    if (hecho.getUbicacion().faltanDatos()) {
      Ubicacion ubiAnterior = hecho.getUbicacion();
      Ubicacion actualizada = IAdapUbicacion.buscarUbicacion(ubiAnterior.getLatitud(), ubiAnterior.getLongitud());
      hecho.setUbicacion(actualizada);
      hecho.getUbicacion().setPais(Pais.ARGENTINA);
      return Mono.just(hecho);
    } else {
      hecho.getUbicacion().setPais(Pais.ARGENTINA);
      return Mono.just(hecho);
    }
  }
}
