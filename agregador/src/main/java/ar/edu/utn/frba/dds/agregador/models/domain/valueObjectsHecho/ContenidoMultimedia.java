package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table()
public class ContenidoMultimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String ruta;

    @Enumerated(EnumType.STRING)
    private Formato formato;

    public ContenidoMultimedia(String ruta) {
        this.ruta = ruta;
    }
}
