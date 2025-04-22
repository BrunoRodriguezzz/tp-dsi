package models.entities.usuarios;

import java.time.LocalDate;
import java.time.Period;

public class Contribuyente {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate nuevaFechaNacimiento) {
        this.fechaNacimiento = nuevaFechaNacimiento;
    }

    public Integer laEdadEs(){
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();
    }
}