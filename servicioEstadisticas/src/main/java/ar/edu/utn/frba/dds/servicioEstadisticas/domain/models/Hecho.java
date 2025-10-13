package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "hecho")
public class Hecho {
    @Id
    private Long id;
}
