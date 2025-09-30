package ar.edu.utn.frba.dds.client.services.internal;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebApiCallerService {
    private final WebClient webClient;

    public WebApiCallerService() {
        this.webClient = WebClient.builder().build();
    }

    /**
     * Ejecuta una llamada HTTP GET
     */
    public <T> T get(String url, Class<T> responseType) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    /**
     * Ejecuta una llamada HTTP GET que retorna una lista
     */
    public <T> java.util.List<T> getList(String url, Class<T> responseType) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(responseType)
                .collectList()
                .block();
    }

    /**
     * Ejecuta una llamada HTTP POST
     */
    public <T> T post(String url, Object body, Class<T> responseType) {
        return webClient
                .post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
    /**
     * Ejecuta una llamada HTTP PUT
     */
    public <T> T put(String url, Object body, Class<T> responseType) {
        return webClient
                .put()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
    /**
     * Ejecuta una llamada HTTP DELETE
     */
    public void delete(String url) {
        webClient
                .delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
