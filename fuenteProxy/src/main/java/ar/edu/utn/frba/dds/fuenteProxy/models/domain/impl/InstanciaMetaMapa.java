package ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class InstanciaMetaMapa implements TipoFuente {
    private WebClient webClient; //TODO Ver como instanciamos distintas instancias de MetaMapa

    @Override
    public Flux<HechoDTO> getAll() {
        //TODO
        return Flux.fromArray(new HechoDTO[] {});
    }

    @Override
    public Mono<HechoDTO> getById(Long id) {
        //TODO
        return null;
    }

    @Override
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol) {
        //TODO
    }
}
