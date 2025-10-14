package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDinamicaDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
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

    public HechoDTO obtenerHechoPorId(Long id) {
        return mockService.obtenerHechoPorId(id);
    }

    public List<HechoDTO> obtenerHechosPendientes() {
        return this.mockService.obtenerHechosPendientesMockeados();
    }

    public void gestionarHecho(@Valid HechoRevisadoForm form) {
        HechoDinamicaDTO dto = HechoDinamicaDTO.builder()
                .idAdministrador(form.getIdAdministrador())
                .id(form.getId())
                .etiquetas(form.getEtiquetas() != null ? List.of(form.getEtiquetas().split(",")) : List.of())
                .estadoHecho(form.getEstadoHecho())
                .sugerenciaDeCambio(form.getSugerenciaDeCambio())
                .build();

        System.out.println("[LOG] Enviando a fuenteDinamica: " + dto);

        try {
            WebClient.create("http://localhost:8081")
                    .post()
                    .uri("/api/fuenteDinamica/gestion")
                    .bodyValue(dto)
                    .retrieve()
                    .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("[ERROR] Falló la comunicación con fuenteDinamica: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
