package ar.edu.utn.frba.dds.agregador.models.domain.usuarios;

import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.FechaInvalidaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contribuyente")
public class Contribuyente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String apellido;

    @Column
    private LocalDateTime fechaNacimiento;

    public Contribuyente(String nombre, String apellido, LocalDateTime fechaNacimiento) throws FechaInvalidaException {
        this.nombre = nombre;
        this.apellido = apellido;
        if(fechaNacimiento == null || fechaNacimiento.isAfter(LocalDateTime.now())) {
            throw new FechaInvalidaException("La fecha es inválida: "+fechaNacimiento);
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer laEdadEs() {
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento.toLocalDate(), hoy).getYears();
    }
}