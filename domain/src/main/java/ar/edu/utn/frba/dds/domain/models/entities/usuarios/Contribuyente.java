package ar.edu.utn.frba.dds.domain.models.entities.usuarios;

import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import lombok.Getter;
import lombok.Setter;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;

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
            throw new FechaInvalidaException("La fecha es inválida: "+fechaNacimiento);
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer laEdadEs() {
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();
    }
}