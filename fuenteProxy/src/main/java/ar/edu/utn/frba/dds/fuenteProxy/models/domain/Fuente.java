package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

import java.util.List;

@Getter
@Setter
public class Fuente {
    private TipoFuente tipoFuente;
    private Long id;
    private String nombre;
    private String ruta;

    public Fuente(TipoFuente tipoFuente, Long id) {
        this.tipoFuente = tipoFuente;
        this.id = id;
    }

    public Flux<HechoDTO> getAllHechos() {
        return tipoFuente.getAll();
    }
}
