package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "estadistica_combinacion")
public class EstadisticaCombinacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coleccion_id")
    Coleccion coleccion;

    @Enumerated(EnumType.STRING)
    Provincia provincia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    Categoria categoria;

    @Enumerated(EnumType.STRING)
    HoraDelDia hora;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "hechos_x_estadistica",
            joinColumns = @JoinColumn(name = "estadistica_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    )
    List<Hecho> hechos;
}