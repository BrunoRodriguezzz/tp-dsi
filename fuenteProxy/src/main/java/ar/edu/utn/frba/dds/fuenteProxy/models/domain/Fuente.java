package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;

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
    private String ruta; // TODO debería usarse.

    public Flux<InputHecho> getAllHechos() {
        return tipoFuente.getAll().map(inputHecho -> {
            inputHecho.setId_fuente(this.id);
            return inputHecho;
        });
    }
}
