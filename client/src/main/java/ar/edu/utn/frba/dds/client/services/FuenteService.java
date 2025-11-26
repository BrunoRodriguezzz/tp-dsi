package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.FuenteDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FuenteService {
    private final MockService mockService;
    private final String urlGateway;
    private final WebApiCallerService webApiCallerService;

    public FuenteService(@Value("${servicio.apiGateway}") String urlGateway, WebApiCallerService webApiCallerService) {
        this.urlGateway = urlGateway;
        this.mockService = new MockService();
        this.webApiCallerService = webApiCallerService;
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

    public String importarCSV(MultipartFile archivo, String nombreFuente) {
        org.springframework.http.client.MultipartBodyBuilder builder = new org.springframework.http.client.MultipartBodyBuilder();
        builder.part("archivo", archivo.getResource());
        builder.part("nombre", nombreFuente);

        try{
            return webApiCallerService.postMultipart(urlGateway + "/archivos/upload", builder, String.class);
        }
        catch(Exception e) {
            log.error("Error al importar el archivo CSV: {}", e.getMessage());
            return "Error al importar el archivo CSV: " + e.getMessage();
        }

    }
}
