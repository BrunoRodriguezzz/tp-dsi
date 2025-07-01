package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Getter
@Setter
public class Fuente {
    private TipoFuente tipoFuente;
    private Long id;
    private String nombre;
    private String ruta;

    public Fuente(TipoFuente tipoFuente, String nombre, String ruta) {
        this.tipoFuente = tipoFuente; // Asumo que se lo contruyo y después se lo seteo.
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public Flux<InputHecho> getAllHechos() {
        return tipoFuente.getAll().map(inputHecho -> {
            inputHecho.setId_fuente(this.id);
            inputHecho.setOrigen(Origen.PROXY);
            return inputHecho;
        });
    }

    public Flux<InputColeccionDTO> getAllColecciones() {
        return tipoFuente.getAllColecciones();
    }

    public Flux<InputHecho> getNuevos(LocalDateTime date) {
        return this.tipoFuente.getNuevos(date);
    }
}
