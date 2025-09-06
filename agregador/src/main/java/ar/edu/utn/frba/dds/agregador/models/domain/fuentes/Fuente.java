package ar.edu.utn.frba.dds.agregador.models.domain.fuentes;

import ar.edu.utn.frba.dds.agregador.converters.TipoFuenteConverter;
import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpC;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpC;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpHDinamico;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpHEstatico;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpHProxy;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

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

  @Transient
  private IAdapImpH iAdapImpH; // TODO

  @Transient
  private IAdapImpC iAdapImpC;

  // TODO: GRITA PATRON FACTORY
  public Fuente(String nombre, String path, TipoFuente tipoFuente, Long idInternoFuente) {
    this.nombre = nombre;
    this.path = path;
    this.tipoFuente = tipoFuente;
    this.idInternoFuente = idInternoFuente;
    // Dependiendo el tipo de fuente se configura el Adapter y otras cuestiones particulares
    this.inicializar();
  }

  public List<Hecho> importarHechos() {
    List<Hecho> hechos = iAdapImpH.importarHechos(this.webClient, this.idInternoFuente);
    hechos.stream().forEach(h -> h.setFuente(this));
    return hechos;
  }

  public List<Hecho> importarHechosMismoTitulo(Hecho hecho) {
    List<Hecho> hechosImportados = iAdapImpH.importarHechosMismoTitulo(this.webClient, this.idInternoFuente, hecho);
    hechosImportados.stream().forEach(h -> h.setFuente(this));
    return hechosImportados;
  }

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    List<Hecho> hechos = iAdapImpH.buscarNuevosHechos(ultimaFechaRefresco, this.webClient, this.idInternoFuente);
    hechos.stream().forEach(h -> h.setFuente(this));
    return hechos;
  }

  public void eliminarHecho(Hecho hecho) {
    this.iAdapImpH.eliminarHecho(hecho, webClient, this.idInternoFuente);
  }

  public List<Coleccion> importarColecciones() {
    List<Coleccion> colecciones = iAdapImpC.importarColecciones(this.webClient, this.idInternoFuente);
    return colecciones;
  }

  public Coleccion importarColeccion(Long id) {
    Coleccion coleccion = iAdapImpC.importarColeccion(this.webClient, id,this.idInternoFuente);
    return coleccion;
  }

  public List<Hecho> importarHechosColeccion(Long id) {
    if(!this.tipoFuente.equals(TipoFuente.PROXY)) {
      throw new RuntimeException("El tipo de fuente no es PROXY, no se le debería pedir Colecciones");
    }
    else {
      List<Hecho> hechos = iAdapImpC.importarHechosColeccion(this.webClient, id, this.idInternoFuente);
      return hechos;
    }
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
        this.iAdapImpH = AdapImpHEstatico.getInstance();
      }
      case PROXY -> {
        this.webClient = WebClient.builder()
                .baseUrl(path)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
                .build();
        this.iAdapImpH = AdapImpHProxy.getInstance();
        this.iAdapImpC = AdapImpC.getInstance();
      }
    }
  }
}