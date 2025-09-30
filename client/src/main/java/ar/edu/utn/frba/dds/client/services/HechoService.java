package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.HechoOutputDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class HechoService {
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String hechoServiceUrl;

    public HechoService(
            WebApiCallerService webApiCallerService,
            @Value("${hechos.service.url}") String hechoServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.hechoServiceUrl = hechoServiceUrl;
    }

    public List<HechoOutputDTO> obtenerHechosDestacados() {
        List<HechoOutputDTO> response = this.webApiCallerService.getList(this.hechoServiceUrl, HechoOutputDTO.class);
        return response != null ? response : List.of();
    }
}
