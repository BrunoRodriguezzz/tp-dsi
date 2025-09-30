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

public interface IAdapUbicacion {
    public static Ubicacion buscarUbicacion(String latitud, String longitud) {
        WebClient client;
        client = WebClient.builder()
            .baseUrl("https://apis.datos.gob.ar/georef/api")
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
            .map(ResponseUbicacionGobierno::toUbicacion)
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
                .map(r -> {
                    return r.getResultados().stream().map(ResponseUbicacionGobierno::toUbicacion).toList();
                });
    }

    public static Flux<Hecho> buscarUbicacionesReactivo(List<Hecho> hechos) {
        if (hechos.isEmpty()) {
            return Flux.empty();
        }

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
                });
    }

}

