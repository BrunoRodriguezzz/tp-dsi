package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "estadistica_hechos")
public class EstadisticaHechos {
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

    Integer cantidad_hechos;
}
