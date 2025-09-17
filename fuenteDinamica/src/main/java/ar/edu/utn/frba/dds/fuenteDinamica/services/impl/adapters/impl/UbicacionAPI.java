package ar.edu.utn.frba.dds.fuenteDinamica.services.impl.adapters.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.adapterDTO.UbicacioninputAPIDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteDinamica.services.impl.adapters.IUbicacionAPI;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UbicacionAPI implements IUbicacionAPI {

    private final WebClient webClient = WebClient
            .builder()
            .baseUrl("https://apis.datos.gob.ar/georef/api")
            .build();

    @Override
    public void buscarUbicacion(Ubicacion ubicacion) {

         UbicacioninputAPIDTO ubicacionConsulta = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ubicacion")
                        .queryParam("lat",Double.parseDouble(ubicacion.getLatitud()))
                        .queryParam("lon",Double.parseDouble(ubicacion.getLongitud()))
                        .build())
                .retrieve()
                .bodyToMono(UbicacioninputAPIDTO.class)
                .block();

        if (ubicacionConsulta != null && ubicacionConsulta.getUbicacion() != null) {

            ubicacion.setPais("Argentina");
            ubicacion.setProvincia(ubicacionConsulta.getUbicacion().getProvincia().getNombre());

            if (ubicacionConsulta.getUbicacion().getMunicipio() != null) {

                ubicacion.setMunicipio(ubicacionConsulta.getUbicacion().getMunicipio().getNombre());

            } else {

                ubicacion.setMunicipio("No encontrado");

            }
        } else {
            throw new RuntimeException("No se pudo obtener la ubicación desde la API Georef.");
        }
    }
}
