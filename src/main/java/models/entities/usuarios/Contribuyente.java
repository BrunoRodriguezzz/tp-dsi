package models.entities.usuarios;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

public class Contribuyente {
    private String nombre;
    private String apellido;
    @Getter
    @Setter
    private LocalDate fechaNacimiento;

    public Integer laEdadEs(){
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();
    }
}