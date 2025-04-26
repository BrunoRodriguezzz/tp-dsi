package models.entities.usuarios;

import lombok.Getter;
import lombok.Setter;
import models.entities.hechos.Hecho;
import models.entities.solicitudEliminacion.SolicitudEliminacion;
import models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import models.entities.utils.Errores.ER_ValueObjects.FundamentoInvalidoException;

import java.time.LocalDate;
import java.time.Period;

public class Contribuyente {
    private String nombre;
    private String apellido;
    @Getter
    @Setter
    private LocalDate fechaNacimiento;

    public Contribuyente(String nombre, String apellido, LocalDate fechaNacimiento) throws FechaInvalidaException {
        this.nombre = nombre;
        this.apellido = apellido;
        if(fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            throw new FechaInvalidaException("La fecha: " + fechaNacimiento + "es una fecha futura");
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer laEdadEs() {
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();
    }
}