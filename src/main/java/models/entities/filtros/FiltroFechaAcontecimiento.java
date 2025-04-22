package models.entities.filtros;

import models.entities.criterios.Filtro;
import models.entities.criterios.RangoFechas;
import models.entities.hechos.Hecho;

public class FiltroFechaAcontecimiento implements Filtro {
    private RangoFechas fechaAcontecimiento;

    public FiltroFechaAcontecimiento(RangoFechas fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public Boolean coincide(Hecho hecho) {
        return this.fechaAcontecimiento.coincide(hecho.getFechaAcontecimiento());
    }
}
