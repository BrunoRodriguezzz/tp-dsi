package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TipoFuente {
    public Flux<HechoDTO> getAll();
    public Mono<HechoDTO> getById(Long id);
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol);
}
