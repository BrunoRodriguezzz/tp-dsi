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

    public Boolean coincide(LocalDate fecha) throws RuntimeException {
        if(!(fecha instanceof LocalDate)){ //TODO Averiguar porque dice que siempre es redundante
            throw new RuntimeException("Fecha Inválida");
        }
        return fecha.equals(this.fechaInicio)
                || (fecha.isAfter(this.fechaInicio) && fecha.isBefore(this.fechaFin))
                || fecha.equals(this.fechaFin);
    }
}