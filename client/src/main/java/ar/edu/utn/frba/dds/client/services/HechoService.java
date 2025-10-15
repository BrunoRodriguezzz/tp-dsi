package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDinamicaDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
import ar.edu.utn.frba.dds.client.dtos.hecho.PaginadoHechoDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class HechoService {
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String hechoServiceUrl;
    private final MockService mockService;
    private final String agregadorURL = "http://localhost:8082";

    public HechoService(
            WebApiCallerService webApiCallerService,
            @Value("${hechos.service.url}") String hechoServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.hechoServiceUrl = hechoServiceUrl;
        this.mockService = new MockService();
    }

    public List<HechoDTO> obtenerHechosDestacados() {
//        List<HechoDTO> response = this.webApiCallerService.getList(this.hechoServiceUrl, HechoDTO.class);
//        return response != null ? response : List.of();
        return this.mockService.obtenerHechosMockeados();
    }

    public List<HechoDTO> obtenerHechos() {
        return mockService.obtenerHechosMockeados();
    }

    public PaginadoHechoDTO obtenerHechosAgregador() {
        try {
            return this.webApiCallerService.get(this.agregadorURL + "/hechos", PaginadoHechoDTO.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public HechoDTO obtenerHechoPorId(Long id) {
        return mockService.obtenerHechoPorId(id);
    }

}
