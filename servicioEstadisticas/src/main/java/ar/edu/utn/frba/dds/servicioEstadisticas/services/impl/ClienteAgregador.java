package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class ClienteAgregador {
  @Value("${servicio.agregador}")
  private static String urlAgregador;

  public static List<ColeccionInputDTO> generarColecciones() {
    List<ColeccionInputDTO> colecciones = buscarColeccionesConHechos();
    return colecciones;
  }

  public static List<HechoInputDTO> generarHechosIndependientes() {
    List<HechoInputDTO> hechos = buscarHechosIndependientes();
    return hechos;
  }

  public static List<SolicitudEliminacionInputDTO> generarSolicitudesInputDTO() {
    List<SolicitudEliminacionInputDTO> solicitudes = buscarSolicitudesEliminacion();
    return solicitudes;
  }
    public static List<ColeccionInputDTO> buscarColeccionesConHechos() {
      WebClient webClient = WebClient.builder()
          .baseUrl(urlAgregador)
          .build();
      try {
        List<ColeccionInputDTO> colecciones = obtenerTodasLasColeccionesSimple(webClient);
        List<ColeccionInputDTO> coleccionesCompletas = Flux.fromIterable(colecciones)
            .flatMap(coleccion -> obtenerTodosLosHechosPorColeccion(webClient, coleccion.getId())
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

    private static List<ColeccionInputDTO> obtenerTodasLasColeccionesSimple(WebClient webClient) {
      try {
        String response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/colecciones")
                .queryParam("all", true)
                .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonResponse = mapper.readTree(response);

        List<ColeccionInputDTO> colecciones = mapper.readValue(
            jsonResponse.get("content").toString(),
            new TypeReference<List<ColeccionInputDTO>>() {
            }
        );

        return colecciones;

      } catch (Exception e) {
        System.err.println("Error al obtener todas las colecciones: " + e.getMessage());
        return new ArrayList<>();
      }
    }

    private static Flux<HechoInputDTO> obtenerTodosLosHechosPorColeccion(WebClient webClient, Long coleccionId) {
      return webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/colecciones/{id}/hechos")
              .queryParam("all", true)
              .build(coleccionId))
          .retrieve()
          .bodyToMono(String.class)
          .map(response -> {
            try {
              ObjectMapper mapper = new ObjectMapper();
              mapper.registerModule(new JavaTimeModule());
              JsonNode jsonResponse = mapper.readTree(response);

              List<HechoInputDTO> hechos = mapper.readValue(
                  jsonResponse.get("content").toString(),
                  new TypeReference<List<HechoInputDTO>>() {
                  }
              );

              return hechos;

            } catch (Exception e) {
              throw new RuntimeException("Error parsing hechos para colección " + coleccionId, e);
            }
          })
          .onErrorReturn(new ArrayList<>())
          .flatMapMany(Flux::fromIterable);
    }

    public static List<HechoInputDTO> buscarHechosIndependientes() {
        WebClient webClient = WebClient.builder()
            .baseUrl(urlAgregador)
            .build();

        try {
            // bloquea y devuelve la lista
            return webClient.get()
              .uri("/hechos/independientes")
              .retrieve()
                  .bodyToFlux(HechoInputDTO.class) // convierte cada objeto del array
                  .collectList()                   // junta todo en una lista
                  .block()
                    .stream()
                    .filter(hecho->hecho.getUbicacion().getProvincia() != null)
                    .toList();

        } catch (Exception e) {
          System.err.println("Error al buscar hechos independientes: " + e.getMessage());
            e.printStackTrace();
          return new ArrayList<>();
        }
    }

    public static List<SolicitudEliminacionInputDTO> buscarSolicitudesEliminacion() {
        WebClient webClient = WebClient.builder()
            .baseUrl(urlAgregador)
            .build();

        try {
          String response = webClient.get()
              .uri("/solicitudesEliminacion")
              .retrieve()
              .bodyToMono(String.class)
              .block();

          ObjectMapper mapper = new ObjectMapper();
          mapper.registerModule(new JavaTimeModule());

          List<SolicitudEliminacionInputDTO> solicitudes = mapper.readValue(
              response,
              new TypeReference<List<SolicitudEliminacionInputDTO>>() {
              }
          );

          return solicitudes;

        } catch (Exception e) {
          System.err.println("Error al buscar solicitudes de eliminación: " + e.getMessage());
          return new ArrayList<>();
        }
    }
}