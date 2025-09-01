package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contribuyente")
public class Contribuyente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contribuyente_id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_de_nacimiento")
    private LocalDate fechaNacimiento;

    @OneToMany(mappedBy = "contribuyente")
    private List<Hecho> hechos;
}
