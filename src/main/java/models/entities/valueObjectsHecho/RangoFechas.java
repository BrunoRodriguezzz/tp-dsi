package models.entities.valueObjectsHecho;

import java.time.LocalDate;

public class RangoFechas {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public RangoFechas(LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        if (fechaInicio == null || fechaFin == null) {
            throw new Exception("Fecha inicial y final no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new Exception("Fecha inicial no puede ser posterior a la final");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Boolean coincide(LocalDate fecha) throws Exception {
        if(fecha == null){
            throw new IllegalArgumentException("Fecha inválida, la fecha es nula");
        }

        return !fecha.isBefore(this.fechaInicio) && !fecha.isAfter(this.fechaFin);
    }
}