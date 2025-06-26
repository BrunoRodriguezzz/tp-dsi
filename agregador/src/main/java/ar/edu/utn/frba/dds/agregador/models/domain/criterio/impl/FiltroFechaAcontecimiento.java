package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.RangoFechas;

import java.time.LocalDate;

public class FiltroFechaAcontecimiento implements Filtro {
    private RangoFechas fechaAcontecimiento;

    public FiltroFechaAcontecimiento(RangoFechas fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public Boolean coincide(Hecho hecho) {
        try {
            LocalDate fecha = hecho.getFechaAcontecimiento();
            if (fecha == null) {
                throw new FechaInvalidaException("Fecha inválida, la fecha es nula");
            }

            LocalDate inicio = this.fechaAcontecimiento.getFechaInicio();
            LocalDate fin = this.fechaAcontecimiento.getFechaFin();

            boolean despuesDeInicio = (inicio == null) || !fecha.isBefore(inicio);
            boolean antesDeFin = (fin == null) || !fecha.isAfter(fin);

            return despuesDeInicio && antesDeFin;
        } catch (FechaInvalidaException e) {
            System.out.println("Error en FiltroFechaAcontecimiento: " + e.getMessage());
            return false;
        }
    }
}