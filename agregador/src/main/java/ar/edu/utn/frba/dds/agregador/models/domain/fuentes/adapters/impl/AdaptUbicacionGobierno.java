package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapUbicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ResponseUbicacionGobierno;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public class AdaptUbicacionGobierno implements IAdapUbicacion {
    private static String path = "https://apis.datos.gob.ar/georef/api";

    @Override
    public Ubicacion buscarUbicacion(String latitud, String longitud) {
        WebClient client;
        client = WebClient.builder()
                .baseUrl(path)
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
}
