package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Pais;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ubicacionGobierno.ColeccionRespuestaGobiernoDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ubicacionGobierno.ResponseUbicacionGobierno;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.UbicacionOutputGobiernoDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.UbicacionesOutputGobiernoDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public interface IAdapUbicacion {
    public static Ubicacion buscarUbicacion(String latitud, String longitud) {
        WebClient client;
        client = WebClient.builder()
            .baseUrl("https://apis.datos.gob.ar/georef/api/v2.0")
            .build();
        return client.get()
            .uri(uriBuilder -> uriBuilder
                .path("/ubicacion")
                .queryParam("lat", latitud)
                .queryParam("lon", longitud)
                .queryParam("aplanar", "true")
                .build())
            .retrieve()
            .bodyToMono(ResponseUbicacionGobierno.class)
            .timeout(Duration.ofSeconds(5))
            .map(ResponseUbicacionGobierno::toUbicacion)
            .onErrorResume(e -> {
                Logger log = LoggerFactory.getLogger(IAdapUbicacion.class);
                log.warn("Error obteniendo ubicacion sincrona desde georef: {}. Devolviendo ubicacion original vacía.", e.getMessage());
                return Mono.just(new Ubicacion(0.0,0.0));
            })
            .block();
    }

    public static Mono<List<Ubicacion>> buscarUbicaciones(List<Ubicacion> ubicaciones) {
        UbicacionesOutputGobiernoDTO output = new UbicacionesOutputGobiernoDTO();
        ubicaciones.forEach(output::agregarUbicacion);
        WebClient client = WebClient.builder()
                .baseUrl("https://apis.datos.gob.ar/georef/api")
                .build();
        return client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/ubicacion")
                        .build())
                .bodyValue(output)
                .retrieve()
                .bodyToMono(ColeccionRespuestaGobiernoDTO.class)
                .timeout(Duration.ofSeconds(5))
                .map(r -> {
                    return r.getResultados().stream().map(ResponseUbicacionGobierno::toUbicacion).toList();
                })
                .onErrorResume(e -> {
                    Logger log = LoggerFactory.getLogger(IAdapUbicacion.class);
                    log.warn("Error obteniendo ubicaciones en lote desde georef: {}. Devolviendo lista vacía.", e.getMessage());
                    return Mono.just(List.of());
                });
    }

    public static Flux<Hecho> buscarUbicacionesReactivo(List<Hecho> hechos) {
        if (hechos.isEmpty()) {
            return Flux.empty();
        }

        Logger log = LoggerFactory.getLogger(IAdapUbicacion.class);

        WebClient client = WebClient.builder()
                .baseUrl("https://apis.datos.gob.ar/georef/api")
                .build();

        // Crear DTO para este lote de ubicaciones
        UbicacionesOutputGobiernoDTO output = new UbicacionesOutputGobiernoDTO();
        hechos.forEach(h -> output.agregarUbicacion(h.getUbicacion()));

        return client.post()
                .uri("/ubicacion")
                .bodyValue(output)
                .retrieve()
                .bodyToMono(ColeccionRespuestaGobiernoDTO.class)
                .timeout(Duration.ofSeconds(5))
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
                                Ubicacion ubicacion = ubicacionesEnriquecidas.get(i);
                                ubicacion.setPais(Pais.ARGENTINA);
                                hechoOriginal.setUbicacion(ubicacion);
                                return hechoOriginal;
                            });
                })
                .onErrorResume(e -> {
                    // Si hubo error consultando la API de georef (timeout, 5xx, etc.), no abortamos todo el pipeline.
                    // Registramos y devolvemos los hechos originales sin enriquecer para que el flujo continúe.
                    log.warn("Error al obtener ubicaciones reactivamente desde georef: {}. Se devolverán hechos sin enriquecer.", e.getMessage());
                    return Flux.fromIterable(hechos);
                });
    }

}
