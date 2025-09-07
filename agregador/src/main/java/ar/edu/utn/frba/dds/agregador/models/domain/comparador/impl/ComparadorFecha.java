package ar.edu.utn.frba.dds.agregador.models.domain.comparador.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.comparador.IComparadorFecha;

import java.time.Duration;
import java.time.LocalDateTime;

public class ComparadorFecha implements IComparadorFecha {
    private final Duration margen;

    private ComparadorFecha(Duration margen) {
        this.margen = margen;
    }

    // Factory methods para diferentes configuraciones
    public static ComparadorFecha conMargenHoras(long horas) {
        return new ComparadorFecha(Duration.ofHours(horas));
    }

    public static ComparadorFecha conMargenMinutos(long minutos) {
        return new ComparadorFecha(Duration.ofMinutes(minutos));
    }

    public static ComparadorFecha conMargenDias(long dias) {
        return new ComparadorFecha(Duration.ofDays(dias));
    }

    public static ComparadorFecha conMargen(Duration margen) {
        return new ComparadorFecha(margen);
    }

    @Override
    public boolean comparar(LocalDateTime fecha1, LocalDateTime fecha2) {
        if (fecha1 == null || fecha2 == null) {
            return false;
        }

        Duration diferencia = Duration.between(fecha1, fecha2).abs();
        return diferencia.minus(margen).isNegative();
    }
}
