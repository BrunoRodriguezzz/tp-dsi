package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.Fuente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coleccion")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean activo; // baja logica

    @Column(nullable = false)
    private String titulo;
    @Column
    private String descripcion;
    @Column(nullable = false)
    private String criterio;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coleccion_x_fuente",
            joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
    )
    private List<Fuente> fuentes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coleccion_x_hecho",
            joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    )
    private List<HechoProxy> hechos;
}
