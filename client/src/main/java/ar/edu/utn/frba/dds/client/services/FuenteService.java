package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.FuenteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FuenteService {
    private final MockService mockService;
    private final String urlGateway;

    public FuenteService(@Value("${servicio.apiGateway}") String urlGateway) {
        this.urlGateway = urlGateway;
        this.mockService = new MockService();
    }

    public List<FuenteDTO> obtenerFuentes() {
        try {
            WebClient client = WebClient.builder().baseUrl(urlGateway).build();
            return client.get()
                    .uri("/fuentes")
                    .retrieve()
                    .bodyToFlux(FuenteDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<FuenteDTO> obtenerFuentesNuevas() {
        return this.mockService.obtenerFuentesMockeadas();
    }
}
