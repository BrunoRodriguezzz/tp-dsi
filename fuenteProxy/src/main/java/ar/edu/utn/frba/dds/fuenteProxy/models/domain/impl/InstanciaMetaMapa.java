package ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class InstanciaMetaMapa implements TipoFuente {
    private WebClient webClient; //TODO Ver como instanciamos distintas instancias de MetaMapa

    public InstanciaMetaMapa(String baseUrl) { // Lo construimos cuando sale del repo.
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Flux<InputHecho> getAll() {
        return webClient.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(InputHecho.class);
    }

    @Override
    public Mono<InputHecho> getById(Long id) {
        return webClient.get()
                .uri("/hechos/{id}", id)
                .retrieve()
                .bodyToMono(InputHecho.class);
    }

    @Override
    public Flux<InputHecho> getNuevos(LocalDateTime fecha) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/hechos")
                .queryParam("fecha_reporte_desde", fecha.toLocalDate())
                .build())
                .retrieve()
                .bodyToFlux(InputHecho.class);
    }

    @Override
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol) {
        webClient.post()
                .uri("/solicitudes")
                .bodyValue(sol)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(); // se prodía usar block(), pero entones sería sincronico
    }
}
