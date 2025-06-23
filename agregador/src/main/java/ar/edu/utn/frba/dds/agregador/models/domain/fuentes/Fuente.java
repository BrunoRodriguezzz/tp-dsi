package ar.edu.utn.frba.dds.agregador.models.domain.fuentes;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpC;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpC;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpHDinamico;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpHEstatico;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl.AdapImpHProxy;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

public class Fuente {
  @Getter
  private String nombre;
  @Getter
  @Setter
  private Long id;
  private String path;
  private WebClient webClient;
  @Getter
  private TipoFuente tipoFuente;
  private IAdapImpH iAdapImpH;
  private IAdapImpC iAdapImpC;

  public Fuente(String nombre, String path, TipoFuente tipoFuente) {
    this.nombre = nombre;
    this.path = path;
    this.tipoFuente = tipoFuente;
    // Dependiendo el tipo de fuente se configura el Adapter y otras cuestiones particulares
    switch(tipoFuente) {
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

  public List<Hecho> importarHechos() {
    List<Hecho> hechos = iAdapImpH.importarHechos(this.webClient);
    return hechos;
  }

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    List<Hecho> hechos = iAdapImpH.buscarNuevosHechos(ultimaFechaRefresco, this.webClient);
    return hechos;
  }

  public void eliminarHecho(Hecho hecho, WebClient webClient) {
    this.iAdapImpH.eliminarHecho(hecho, webClient);
  }

  public List<Coleccion> importarColecciones() {
    List<Coleccion> colecciones = iAdapImpC.importarColecciones(this.webClient);
    return colecciones;
  }

  public Coleccion importarColeccion(Long id) {
    Coleccion coleccion = iAdapImpC.importarColeccion(this.webClient, id);
    return coleccion;
  }

  public List<Hecho> importarHechosColeccion(Long id) {
    if(!this.tipoFuente.equals(TipoFuente.PROXY)) {
      throw new RuntimeException("El tipo de fuente no es PROXY, no se le debería pedir Colecciones");
    }
    else {
      List<Hecho> hechos = iAdapImpC.importarHechosColeccion(this.webClient, id);
      return hechos;
    }
  }
}