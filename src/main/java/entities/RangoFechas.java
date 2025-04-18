package entities;

import java.util.Date;

public class RangoFechas implements ElementoCriterio<Date> {
    private Date fechaInicio;
    private Date fechaFin;

    public RangoFechas(Date fechaInicio, Date fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public Boolean coincide(Date fecha) throws RuntimeException {
        if(!(fecha instanceof Date)){ //TODO Averiguar porque dice que siempre es redundante
            throw new RuntimeException("Fecha Inválida");
        }
        return fecha.equals(this.fechaInicio)
                || (fecha.after(this.fechaInicio) && fecha.before(this.fechaFin))
                || fecha.equals(this.fechaFin);
    }
}