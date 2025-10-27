package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.estadisticas.*;
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
    private final String estadisticasServiceUrl;
    private final MockService mockService;

    public EstadisticaService(
            WebApiCallerService webApiCallerService,
            @Value("${estadisticas.service.url}") String estadisticasServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.estadisticasServiceUrl = estadisticasServiceUrl;
        this.mockService = new MockService();
    }

    public EstadisticaCategoriaDTO getCategoriaTop(){
        return this.webApiCallerService.get(this.estadisticasServiceUrl + "/categoria/mayorCantidadHechos", EstadisticaCategoriaDTO.class);
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
        try {
            return this.webApiCallerService.get(this.estadisticasServiceUrl + "/solicitudes/cantSpam", EstadisticaSolicitudesDTO.class);
        } catch (Exception e) {
            return this.mockService.getCantSolicitudesSpam();
        }
    }

    public List<EstadisticaProvinciaXColeccionDTO> getRankingProvinciasPorColeccion() {
        return this.mockService.getRankingProvinciasPorColeccion();
    }

    public List<EstadisticaHoraXCategoriaDTO> getHorariosPorCategoria(){
        return this.mockService.getHorariosPorCategoria(this.mockService.getCategorias());
    }
}
