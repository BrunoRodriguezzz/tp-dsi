package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.estadisticas.*;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
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
            @Value("${servicio.apiGateway}") String gatewayURL) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.estadisticasServiceUrl = gatewayURL + "/estadisticas";
        this.mockService = new MockService();
    }

    public EstadisticaCategoriaDTO getRankingCategorias(){
        return this.webApiCallerService.get(this.estadisticasServiceUrl + "/categorias/mayorCantidadHechos", EstadisticaCategoriaDTO.class);
    }

    public List<EstadisticaProvinciaXCategoriaDTO> getCategoriasPorProvincias(){
        try {
            return this.webApiCallerService.getList(this.estadisticasServiceUrl + "/provincias/categorias/mayorCantidadHechos", EstadisticaProvinciaXCategoriaDTO.class);
        } catch (Exception e) {
            return this.mockService.getCategoriasPorProvincia().entrySet().stream()
                    .map(entry -> new EstadisticaProvinciaXCategoriaDTO(
                            entry.getKey(),     // la categoría
                            entry.getValue()    // el mapa de provincias y sus cantidades
                    ))
                    .collect(Collectors.toList());
        }
    }

    public EstadisticaSolicitudesDTO getCantSolicitudesSpam(){
        try {
            return this.webApiCallerService.get(this.estadisticasServiceUrl + "/solicitudes/cantSpam", EstadisticaSolicitudesDTO.class);
        } catch (Exception e) {
            return this.mockService.getCantSolicitudesSpam();
        }
    }

    public List<EstadisticaProvinciaXColeccionDTO> getRankingProvinciasPorColeccion() {
        try {
            return this.webApiCallerService.getList(this.estadisticasServiceUrl + "/colecciones/provincias", EstadisticaProvinciaXColeccionDTO.class);
        }catch (Exception e){
            return this.mockService.getRankingProvinciasPorColeccion();
        }
    }

    public List<EstadisticaHoraXCategoriaDTO> getHorariosPorCategoria(){
        try {
            return this.webApiCallerService.getList(this.estadisticasServiceUrl + "/categorias/hora/mayorCantidadHechos", EstadisticaHoraXCategoriaDTO.class);
        } catch (Exception e) {
            return this.mockService.getHorariosPorCategoria(this.mockService.getCategorias());
        }
    }

    // NOTA: si hay tiempo, esto se podría eliminar y manejar una List en vez de un Map en el JS, pero por ahora lo dejo así
    public Map<String, Map<String, Long>> convertToMap(List<EstadisticaProvinciaXColeccionDTO> estadisticasProvinciasPorColecciones){
        return estadisticasProvinciasPorColecciones.stream()
                .collect(Collectors.toMap(
                        EstadisticaProvinciaXColeccionDTO::getColeccion,
                        EstadisticaProvinciaXColeccionDTO::getProvinciasConHechos,
                        (existing, replacement) -> existing,  // merge function (en caso de duplicados)
                        LinkedHashMap::new
                ));
    }
}
