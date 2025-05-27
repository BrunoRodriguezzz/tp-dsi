package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

@Setter
@Getter
public class Archivo {
    private Long id;
    private String nombre;
    private String rutaArchivo;
    // TODO: me llevo la fuenteEstatica del Dominio?? porque esa interfaz Fuente en realidad sería el tipo de Archivo de Fuente Estática
    private TipoArchivo tipoArchivo;

    public Flux<HechoEstatica> importarHechos(){
        return tipoArchivo.importarHechos(rutaArchivo).map(h -> {
            h.setIdArchivo(this.id);
            return h;
        });
    }
}
