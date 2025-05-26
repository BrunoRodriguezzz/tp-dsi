package ar.edu.utn.frba.dds.fuenteProxy.models.domain;


import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TipoFuente {
    public Flux<InputHecho> getAll();
    public Mono<InputHecho> getById(Long id);
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol);
}
