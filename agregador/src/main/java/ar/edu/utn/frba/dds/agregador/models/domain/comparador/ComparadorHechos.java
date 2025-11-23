package ar.edu.utn.frba.dds.agregador.models.domain.comparador;

import ar.edu.utn.frba.dds.agregador.models.domain.comparador.impl.ComparadorFecha;
import ar.edu.utn.frba.dds.agregador.models.domain.comparador.impl.ComparadorUbicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class ComparadorHechos {
    IComparadorFecha comparadorFecha;
    IComparacionUbicacion comparadorUbicacion;

    public ComparadorHechos() {
        this.comparadorFecha = ComparadorFecha.conMargenHoras(1);
        this.comparadorUbicacion = ComparadorUbicacion.conRadioKilometros(1);
    }

    public boolean comparar(Hecho h1, Hecho h2) {
        if (h1 == null || h2 == null) {
            return false;
        }

        if (h1.getFechaAcontecimiento() == null || h2.getFechaAcontecimiento() == null ||
                h1.getUbicacion() == null || h2.getUbicacion() == null ||
                h1.getCategoria() == null || h2.getCategoria() == null) {
            return false;
        }

        LocalDateTime fecha1 = h1.getFechaAcontecimiento();
        LocalDateTime fecha2 = h2.getFechaAcontecimiento();

        return this.comparadorFecha.comparar(fecha1, fecha2) &&
                this.comparadorUbicacion.comparar(h1.getUbicacion(), h2.getUbicacion()) &&
                this.compararCategoria(h1, h2);
    }

    private boolean compararCategoria(Hecho h1, Hecho h2) {
        return h1.getCategoria().getTitulo().equalsIgnoreCase(h2.getCategoria().getTitulo());
    }
}
