package entities;

import java.time.LocalDate;

public class RangoFechas implements ElementoCriterio<LocalDate> {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Override
    public Boolean coincide(LocalDate fecha) throws RuntimeException {
        if(!(fecha instanceof LocalDate)){ //TODO Averiguar porque dice que siempre es redundante
            throw new RuntimeException("Fecha Inválida");
        }
        return  fecha.isEqual(this.fechaInicio)
                || fecha.isEqual(this.fechaInicio)
                || (fecha.isAfter(this.fechaInicio) && fecha.isBefore(this.fechaFin));
    }
}