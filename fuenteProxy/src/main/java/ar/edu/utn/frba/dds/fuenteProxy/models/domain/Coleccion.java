package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

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

    @Column(nullable = false)
    private String titulo;
    @Column
    private String descripcion;
    @Column(nullable = false)
    private String criterio;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Fuente> fuentes;


    private List<Long> idsHechos;
}
