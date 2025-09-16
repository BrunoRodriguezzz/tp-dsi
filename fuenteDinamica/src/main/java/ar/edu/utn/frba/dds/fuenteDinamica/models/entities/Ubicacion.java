package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.valueObjects.Pais;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.valueObjects.Provincia;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @Enumerated(EnumType.STRING)
    private Pais Pais;

    @Enumerated(EnumType.STRING)
    private Provincia Provincia;

    @Column(name = "municipio")
    private String municipio;
}