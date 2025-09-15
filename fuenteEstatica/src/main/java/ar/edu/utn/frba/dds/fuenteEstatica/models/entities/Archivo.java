package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.fuenteEstatica.converters.TipoArchivoConverter;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl.ArchivoCSV;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "archivo")
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String rutaArchivo;

    @Convert(converter = TipoArchivoConverter.class)
    @Column(name = "tipoArchivo")
    private TipoArchivo tipoArchivo;

    public Flux<HechoEstatica> importarHechos(){
        return tipoArchivo.importarHechos(rutaArchivo).map(h -> {
            h.setIdArchivo(this.id);
            return h;
        });
    }
}
