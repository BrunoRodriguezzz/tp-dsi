package models.entities.filtros;

import models.entities.criterios.ElementoCriterio;

import java.time.LocalDate;

public class RangoFechas {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public RangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Boolean coincide(LocalDate fecha) {
        if(fecha == null){ //TODO Averiguar porque dice que siempre es redundante
            throw new RuntimeException("Fecha Inválida");
        }
        return !fecha.isBefore(this.fechaInicio) && !fecha.isAfter(this.fechaFin);
    }
}