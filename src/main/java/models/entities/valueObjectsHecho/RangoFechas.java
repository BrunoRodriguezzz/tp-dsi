package models.entities.valueObjectsHecho;

import models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;

import java.time.LocalDate;

public class RangoFechas {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public RangoFechas(LocalDate fechaInicio, LocalDate fechaFin) throws FechaInvalidaException {
        if (fechaInicio == null || fechaFin == null) {
            throw new FechaInvalidaException("Fecha inicial y final no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new FechaInvalidaException("Fecha inicial no puede ser posterior a la final");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Boolean coincide(LocalDate fecha) throws FechaInvalidaException {
        if(fecha == null){
            throw new FechaInvalidaException("Fecha inválida, la fecha es nula");
        }

        return !fecha.isBefore(this.fechaInicio) && !fecha.isAfter(this.fechaFin);
    }
}