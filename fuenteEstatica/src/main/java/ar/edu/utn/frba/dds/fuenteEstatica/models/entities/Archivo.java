package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.fuenteEstatica.converters.tipoFuenteConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;

@Setter
@Getter
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String rutaArchivo;

    @Convert(converter = tipoFuenteConverter.class) // TODO
    @Column(nullable = false)
    private TipoArchivo tipoArchivo; // TODO: hay que ver el tema del converter

    public Flux<HechoEstatica> importarHechos(){
        return tipoArchivo.importarHechos(rutaArchivo).map(h -> {
            h.setIdArchivo(this.id);
            return h;
        });
    }
}
