package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.EstadisticaCategoriaDTO;
import ar.edu.utn.frba.dds.client.dtos.EstadisticaProvinciaXCategoriaDTO;
import ar.edu.utn.frba.dds.client.dtos.EstadisticaProvinciaXColeccionDTO;
import ar.edu.utn.frba.dds.client.dtos.EstadisticaSolicitudesDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
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

    public EstadisticaCategoriaDTO getCategorias(){
        return new EstadisticaCategoriaDTO(this.mockService.getCategorias());
    }

    public List<EstadisticaProvinciaXCategoriaDTO> getCategoriasPorProvincias(){
        Map<String, Map<String, Long>> categoriasPorProvincia = this.mockService.getCategoriasPorProvincia();

        return categoriasPorProvincia.entrySet().stream()
                .map(entry -> new EstadisticaProvinciaXCategoriaDTO(
                        entry.getKey(),     // la categoría
                        entry.getValue()    // el mapa de provincias y sus cantidades
                ))
                .collect(Collectors.toList());

    }

    public EstadisticaSolicitudesDTO getCantSolicitudesSpam(){
        return this.mockService.getCantSolicitudesSpam();
    }

    public List<EstadisticaProvinciaXColeccionDTO> getRankingProvinciasPorColeccion() {
        return this.mockService.getRankingProvinciasPorColeccion();
    }
}
