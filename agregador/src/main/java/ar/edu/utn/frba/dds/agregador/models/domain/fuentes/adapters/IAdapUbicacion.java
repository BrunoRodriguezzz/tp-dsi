package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ResponseUbicacionGobierno;
import org.springframework.web.reactive.function.client.WebClient;

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
}
