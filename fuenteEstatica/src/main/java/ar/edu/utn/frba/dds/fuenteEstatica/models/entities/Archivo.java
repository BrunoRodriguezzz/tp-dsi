package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.fuenteEstatica.converters.tipoArchivoConverter;
import ar.edu.utn.frba.dds.fuenteEstatica.models.enums.TipoArchivoEnum;
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

    @Transient
    private TipoArchivo tipoArchivo;

    @Column(nullable = false)
    @Convert(converter = tipoArchivoConverter.class)
    private TipoArchivoEnum tipoArchivoEnum;

    public Flux<HechoEstatica> importarHechos(){
        return tipoArchivo.importarHechos(rutaArchivo).map(h -> {
            h.setIdArchivo(this.id);
            return h;
        });
    }
}
