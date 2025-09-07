package ar.edu.utn.frba.dds.agregador.models.domain.comparador;

import java.time.LocalDateTime;

public interface IComparadorFecha {
    public boolean comparar(LocalDateTime fecha1, LocalDateTime fecha2);
}
