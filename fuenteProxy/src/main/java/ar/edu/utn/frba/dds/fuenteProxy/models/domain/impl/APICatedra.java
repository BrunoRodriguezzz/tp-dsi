package ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.PaginaHechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores.RequestLogin;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores.ResponseLogin;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

//@Component
public class APICatedra implements TipoFuente {
    private WebClient webClient;
    //private final WebClient.Builder webClientBuilder;
    private String token;

    public APICatedra(WebClient.Builder builder) { //TODO: Podría llegar a ser asincrónico
        this.webClient = WebClient.builder().baseUrl("https://api-ddsi.disilab.ar/public").build();

        this.token = login();

        this.webClient = WebClient.builder()
                .baseUrl("https://api-ddsi.disilab.ar/public")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }
//    public APICatedra(WebClient.Builder builder) {
//        this.webClientBuilder = builder;
//    }
//
//    @PostConstruct
//    public void init() {
//        this.token = login(); // Pedís el token después de que el bean fue creado
//        this.webClient = webClientBuilder
//                .baseUrl("https://api-ddsi.disilab.ar/public")
//                .defaultHeader("Authorization", "Bearer " + token)
//                .build();
//    }

//    private String login() {
//        RequestLogin requestLogin = new RequestLogin("dds@gmail.com", "ddsi2025*");
//
//        WebClient tempClient = webClientBuilder
//                .baseUrl("https://api-ddsi.disilab.ar/public")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//
//        ResponseLogin response = tempClient
//                .post()
//                .uri("/api/login")
//                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
//                .bodyValue(requestLogin)
//                .retrieve()
//                .onStatus(
//                        status -> status.is4xxClientError() || status.is5xxServerError(),
//                        clientResponse -> Mono.error(new RuntimeException("Falló el login"))
//                )
//                .bodyToMono(ResponseLogin.class)
//                .block(); // Sin block() no podés obtener el token sincrónicamente
//
//        return response.getData().getAccess_token();
//    }

    private String login() {
        RequestLogin requestLogin = new RequestLogin("dds@gmail.com", "ddsi2025*");
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
