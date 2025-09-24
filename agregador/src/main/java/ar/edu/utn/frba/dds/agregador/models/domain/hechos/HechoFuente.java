package ar.edu.utn.frba.dds.agregador.models.domain.hechos;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hecho_fuente")
@Data
public class HechoFuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // PK técnica, más simple

    @ManyToOne
    @JoinColumn(name = "hecho_id", nullable = false)
    private Hecho hecho;

    @ManyToOne
    @JoinColumn(name = "fuente_id", nullable = false)
    private Fuente fuente;

    @Column(name = "id_interno_fuente", nullable = false)
    private Long idInternoFuente;
}