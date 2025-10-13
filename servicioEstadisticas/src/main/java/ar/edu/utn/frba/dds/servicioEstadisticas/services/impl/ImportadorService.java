package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IImportadorService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ImportadorService implements IImportadorService {

    private final WebClient webClient;

    public ImportadorService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    @Override
    public List<HechoInputDTO> importarHechos() {
        /*try {
            List<HechoInputDTO> hechos = webClient
                    .get()
                    .uri("/hechos")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<HechoInputDTO>>() {})
                    .block();

            return hechos != null ? hechos : new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException("Error al importar hechos: " + e.getMessage());
        }*/
        return GeneradorDatosPrueba.generarHechosIndependientes();
    }

    @Override
    public List<ColeccionInputDTO> importarColecciones() {
        /*try {
            List<ColeccionInputDTO> colecciones = webClient
                    .get()
                    .uri("/colecciones")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ColeccionInputDTO>>() {})
                    .block();
            return colecciones != null ? colecciones : new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException("Error al importar colecciones: " + e.getMessage());
        }*/
        return GeneradorDatosPrueba.generarColecciones();
    }

    @Override
    public List<SolicitudEliminacionInputDTO> importarSolicitudes() {
        return GeneradorDatosPrueba.generarSolicitudesInputDTO();
    }
}
