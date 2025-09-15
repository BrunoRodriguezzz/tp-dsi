package ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente;


import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TipoFuente {
    public Flux<InputHecho> getAll();
    public Mono<InputHecho> getById(Long id);
    public Flux<InputHecho> getNuevos(LocalDateTime date);
    public Flux<InputColeccionDTO> getAllColecciones();
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol);
}
