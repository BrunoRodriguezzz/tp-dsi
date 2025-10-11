package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.EstadisticaCategoriaDTO;
import ar.edu.utn.frba.dds.client.dtos.EstadisticaSolicitudesDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String hechoServiceUrl;
    private final MockService mockService;

    public EstadisticaService(
            WebApiCallerService webApiCallerService,
            @Value("${hechos.service.url}") String hechoServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.hechoServiceUrl = hechoServiceUrl;
        this.mockService = new MockService();
    }

    public EstadisticaCategoriaDTO getCategoriaTop(){
        Map<String, Long> categorias = this.mockService.getCategoriaTop();

        // Ordenar por cantidad descendente y limitar a 30 (mantengo LinkedHashMap para orden predecible)
        Map<String, Long> ordenadoLimitado = categorias.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(30)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        return new EstadisticaCategoriaDTO(ordenadoLimitado);
    }

    public EstadisticaSolicitudesDTO getCantSolicitudesSpam(){
        return this.mockService.getCantSolicitudesSpam();
    }
}
