package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.clients.AgregadorClient;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ClienteAgregador {
  private final AgregadorClient agregador;

  public ClienteAgregador(AgregadorClient agregador) {
    this.agregador = agregador;
  }

  public List<ColeccionInputDTO> generarColecciones() {
    List<ColeccionInputDTO> colecciones = buscarColeccionesConHechos();
    return colecciones;
  }

  public List<HechoInputDTO> generarHechosIndependientes() {
    List<HechoInputDTO> hechos = buscarHechosIndependientes();
    return hechos;
  }

  public List<SolicitudEliminacionInputDTO> generarSolicitudesInputDTO() {
    List<SolicitudEliminacionInputDTO> solicitudes = buscarSolicitudesEliminacion();
    return solicitudes;
  }
    public  List<ColeccionInputDTO> buscarColeccionesConHechos() {
      try {
        List<ColeccionInputDTO> colecciones = obtenerTodasLasColeccionesSimple();
        List<ColeccionInputDTO> coleccionesCompletas = Flux.fromIterable(colecciones)
            .flatMap(coleccion -> obtenerTodosLosHechosPorColeccion(coleccion.getId())
                .collectList()
                .map(hechos -> {
                  coleccion.setHechos(hechos);
                  return coleccion;
                }))
            .collectList()
            .block();

        return coleccionesCompletas != null ? coleccionesCompletas : new ArrayList<>();

      } catch (Exception e) {
        System.err.println("Error al buscar colecciones con hechos: " + e.getMessage());
        return new ArrayList<>();
      }
    }

    private List<ColeccionInputDTO> obtenerTodasLasColeccionesSimple() {
      try {
        List<ColeccionInputDTO> colecciones = Objects.requireNonNull(this.agregador.buscarColecciones(true).getBody()).getContent();
        return colecciones;
      } catch (Exception e) {
        System.err.println("Error al obtener todas las colecciones: " + e.getMessage());
        return new ArrayList<>();
      }
    }

    private Flux<HechoInputDTO> obtenerTodosLosHechosPorColeccion(Long coleccionId) {
      try {
        List<HechoInputDTO> hechos = Objects.requireNonNull(this.agregador.buscarHechosColeccion(coleccionId, null, null, null,
                  null, null, null, null,
                  null, null, true, null).getBody()).getContent();
        return Flux.fromIterable(hechos);
      } catch (Exception e) {
        System.err.println("Error al obtener hechos para la colección " + coleccionId + ": " + e.getMessage());
        return Flux.empty();
      }
    }

    public List<HechoInputDTO> buscarHechosIndependientes() {
        try {
            // bloquea y devuelve la lista
            return this.agregador.buscarHechos().getBody();

        } catch (Exception e) {
          System.err.println("Error al buscar hechos independientes: " + e.getMessage());
            e.printStackTrace();
          return new ArrayList<>();
        }
    }

    public List<SolicitudEliminacionInputDTO> buscarSolicitudesEliminacion() {
        try {
          List<SolicitudEliminacionInputDTO> solicitudes = this.agregador.buscarSolicitudes().getBody();
          return solicitudes;

        } catch (Exception e) {
          System.err.println("Error al buscar solicitudes de eliminación: " + e.getMessage());
          return new ArrayList<>();
        }
    }
}