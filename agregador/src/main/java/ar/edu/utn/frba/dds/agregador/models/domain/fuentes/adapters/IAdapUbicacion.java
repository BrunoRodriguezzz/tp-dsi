package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Pais;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ubicacionGobierno.ColeccionRespuestaGobiernoDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ubicacionGobierno.ResponseUbicacionGobierno;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.UbicacionesOutputGobiernoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public interface IAdapUbicacion {
  WebClient WEB_CLIENT = WebClient.builder()
      .baseUrl("https://apis.datos.gob.ar/georef/api")
      .exchangeStrategies(ExchangeStrategies.builder()
          .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // 16 MB
          .build())
      .build();

  Logger log = LoggerFactory.getLogger(IAdapUbicacion.class);

  static Ubicacion buscarUbicacion(String latitud, String longitud) {
    return WEB_CLIENT.get()
        .uri(uriBuilder -> uriBuilder
            .path("/v2.0/ubicacion")
            .queryParam("lat", latitud)
            .queryParam("lon", longitud)
            .queryParam("aplanar", "true")
            .build())
        .retrieve()
        .bodyToMono(ResponseUbicacionGobierno.class)
        .timeout(Duration.ofSeconds(10)) // Timeout preventivo
        .map(ResponseUbicacionGobierno::toUbicacion)
        .onErrorResume(e -> {
          log.warn("Error obteniendo ubicacion sincrona desde georef: {}. Devolviendo ubicacion vacía.", e.getMessage());
          return Mono.just(new Ubicacion(0.0, 0.0));
        })
        .block();
  }

  static Mono<List<Ubicacion>> buscarUbicaciones(List<Ubicacion> ubicaciones) {
    UbicacionesOutputGobiernoDTO output = new UbicacionesOutputGobiernoDTO();
    ubicaciones.forEach(output::agregarUbicacion);

    return WEB_CLIENT.post()
        .uri("/ubicacion")
        .bodyValue(output)
        .retrieve()
        .bodyToMono(ColeccionRespuestaGobiernoDTO.class)
        .timeout(Duration.ofSeconds(30)) // Aumentado a 30s para lotes
        .map(r -> r.getResultados().stream().map(ResponseUbicacionGobierno::toUbicacion).toList())
        .onErrorResume(e -> {
          log.warn("Error obteniendo ubicaciones en lote desde georef: {}. Devolviendo lista vacía.", e.getMessage());
          return Mono.just(List.of());
        });
  }

  static Flux<Hecho> buscarUbicacionesReactivo(List<Hecho> hechos) {
    if (hechos.isEmpty()) {
      return Flux.empty();
    }

    UbicacionesOutputGobiernoDTO output = new UbicacionesOutputGobiernoDTO();
    hechos.forEach(h -> output.agregarUbicacion(h.getUbicacion()));

    return WEB_CLIENT.post()
        .uri("/ubicacion")
        .bodyValue(output)
        .retrieve()
        .bodyToMono(ColeccionRespuestaGobiernoDTO.class)
        .timeout(Duration.ofSeconds(30))
        .flatMapMany(response -> {
          List<Ubicacion> ubicacionesEnriquecidas = response.getResultados().stream()
              .map(ResponseUbicacionGobierno::toUbicacion)
              .toList();

          if (ubicacionesEnriquecidas.size() != hechos.size()) {
            return Flux.error(new RuntimeException(
                "Cantidad de ubicaciones no coincide: request=" + hechos.size() +
                    " response=" + ubicacionesEnriquecidas.size()
            ));
          }

          return Flux.range(0, hechos.size())
              .map(i -> {
                Hecho hechoOriginal = hechos.get(i);
                Ubicacion nuevaUbicacion = ubicacionesEnriquecidas.get(i);

                if (nuevaUbicacion != null) {
                  nuevaUbicacion.setPais(Pais.ARGENTINA);
                  hechoOriginal.setUbicacion(nuevaUbicacion);
                }
                return hechoOriginal;
              });
        })
        .onErrorResume(e -> {
          log.warn("⚠️ Fallo Georef Reactivo: {}. Se usan datos originales.", e.getMessage());
          return Flux.fromIterable(hechos);
        });
  }
}