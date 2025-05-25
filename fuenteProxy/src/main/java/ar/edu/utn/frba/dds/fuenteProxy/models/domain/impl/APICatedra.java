package ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.PaginaHechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores.RequestLogin;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores.ResponseLogin;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class APICatedra implements TipoFuente {
    private WebClient webClient;
    private String token;

    public APICatedra(WebClient.Builder builder) {
        this.webClient = WebClient.builder().baseUrl("https://api-ddsi.disilab.ar/public").build();
    }

    @PostConstruct
    public void init() {
        this.token = login(); // Pedís el token después de que el bean fue creado
        this.webClient = WebClient.builder()
                .baseUrl("https://api-ddsi.disilab.ar/public")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    private String login() {
        RequestLogin requestLogin = new RequestLogin("ddsi@gmail.com", "ddsi2025*");
        ResponseLogin response = this.webClient.post()
                .uri("/api/login") // Dirección
                .bodyValue(requestLogin) // Agrego al body mi request
                .retrieve() // Envío la petición
                .onStatus( // Acá chequeamos que no haya algún problema
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Falló el login"))
                )
                .bodyToMono(ResponseLogin.class) // Le dice a Spring: "cuando recibas la respuesta del servidor, convertí (deserializá) el body del JSON a una instancia de esta clase".
                .block(); // Lo hago sincrónico (no puedo pedir nada hasta que tenga esto).

        return response.getData().getAccess_token();
    }

    public Flux<HechoDTO> getAll() {
        return getHechosPagina("https://api-ddsi.disilab.ar/public/api/desastres");
    }

    private Flux<HechoDTO> getHechosPagina(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(PaginaHechoDTO.class)
                .flatMapMany(pagina -> {
                    Flux<HechoDTO> hechosActuales = Flux.fromIterable(pagina.getData());

                    if (pagina.getNext_page_url() != null) {
                        // Concatenamos los hechos actuales con los de la siguiente página (recursivo)
                        return hechosActuales.concatWith(getHechosPagina(pagina.getNext_page_url()));
                    } else {
                        return hechosActuales;
                    }
                });
    }

    @Override
    public Mono<HechoDTO> getById(Long id) {
        return webClient
                .get()
                .uri("/api/desastres/{id}", id)
                .retrieve()
                .bodyToMono(HechoDTO.class);
    }

    @Override
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol) {
        //TODO
    }
}
