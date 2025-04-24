package models.entities.criterio;

import models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import models.entities.valueObjectsHecho.RangoFechas;
import models.entities.hechos.Hecho;

public class FiltroFechaAcontecimiento implements Filtro {
    private RangoFechas fechaAcontecimiento;

    public FiltroFechaAcontecimiento(RangoFechas fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public Boolean coincide(Hecho hecho) {
        Boolean resultado = false; //Si falla el try catch no debería agregar el hecho por poseer un error
        try{
            resultado = this.fechaAcontecimiento.coincide(hecho.getFechaAcontecimiento());
        }
        catch(FechaInvalidaException e){
            System.out.println("Error en FiltroFechaAcontecimiento: " + e.getMessage());
        }
        return resultado;
    }
}
