package entities;

import java.time.LocalDate;

public class RangoFechas implements ElementoCriterio<LocalDate> {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Override
    public Boolean coincide(LocalDate fecha) throws Exception {
        if(!(fecha instanceof LocalDate)){
            throw new Exception("Fecha Inválida");
        }
        return  fecha.isEqual(this.fechaInicio)
                || fecha.isEqual(this.fechaInicio)
                || (fecha.isAfter(this.fechaInicio) && fecha.isBefore(this.fechaFin));
    }
}