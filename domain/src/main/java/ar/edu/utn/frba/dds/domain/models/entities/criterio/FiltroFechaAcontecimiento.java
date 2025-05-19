package ar.edu.utn.frba.dds.domain.models.entities.criterio;

import models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.RangoFechas;
import models.entities.hechos.Hecho;

import java.time.LocalDate;

public class FiltroFechaAcontecimiento implements Filtro {
    private RangoFechas fechaAcontecimiento;

    public FiltroFechaAcontecimiento(RangoFechas fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public Boolean coincide(Hecho hecho) {
        Boolean resultado = false; //Si falla el try catch no debería agregar el hecho por poseer un error
        try{
            LocalDate fecha = hecho.getFechaAcontecimiento();
            if(fecha == null){
                throw new FechaInvalidaException("Fecha inválida, la fecha es nula");
            }
            return !fecha.isBefore(this.fechaAcontecimiento.getFechaInicio()) && !fecha.isAfter(this.fechaAcontecimiento.getFechaFin());
        }
        catch(FechaInvalidaException e){
            // TODO: Catchea el controller
            System.out.println("Error en FiltroFechaAcontecimiento: " + e.getMessage());
        }
        return resultado;
    }
}
