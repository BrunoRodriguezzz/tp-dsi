package models.entities.filtros;

import models.entities.criterios.ElementoCriterio;
import models.entities.hechos.Hecho;

public class ElementoCriterioFechaAcontecimiento implements ElementoCriterio {
    private RangoFechas fechaAcontecimiento;

    public ElementoCriterioFechaAcontecimiento(RangoFechas fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public Boolean coincide(Hecho hecho) {
        return this.fechaAcontecimiento.coincide(hecho.getFechaAcontecimiento());
    }
}
