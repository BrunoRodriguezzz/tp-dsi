package models.entities.usuarios;

import lombok.Getter;
import lombok.Setter;
import models.entities.hechos.Hecho;
import models.entities.solicitudEliminacion.SolicitudEliminacion;

import java.time.LocalDate;
import java.time.Period;

public class Contribuyente {
    private String nombre;
    private String apellido;
    @Getter
    @Setter
    private LocalDate fechaNacimiento;

    public Contribuyente(String nombre, String apellido, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer laEdadEs() {
        LocalDate hoy = LocalDate.now();
        return Period.between(fechaNacimiento, hoy).getYears();
    }

    public SolicitudEliminacion crearSolicitudEliminacion(Hecho hecho, String fundamento) {
        SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(hecho, fundamento, this);
        return solicitudEliminacion;
    }
}