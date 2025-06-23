package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpC;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

public class AdapImpC implements IAdapImpC {
  // Clase Singleton
  @Getter // Método público para obtener las instancia
  private static final AdapImpC instance = new AdapImpC(); // Creacion de la instancia clase única
  private AdapImpC() {} // Constructor privado para evitar instanciación externa

  public List<Coleccion> importarColecciones(WebClient webClient) {
    //TODO: Implementar metodos + funcionalidad en proxy
    return new ArrayList<Coleccion>();
  }
  public Coleccion importarColeccion(WebClient webClient, Long id) {
    //TODO: Implementar metodos + funcionalidad en proxy
    return null;
  }
  public List<Hecho> importarHechosColeccion(WebClient webClient, Long id) {
    //TODO: Implementar metodos + funcionalidad en proxy
    return null;
  }
}
