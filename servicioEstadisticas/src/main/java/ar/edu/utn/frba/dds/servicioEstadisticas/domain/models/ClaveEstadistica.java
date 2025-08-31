package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import lombok.Data;

import java.util.Objects;

@Data
public class ClaveEstadistica {
    private Coleccion coleccion;
    private Provincia provincia;
    private Categoria categoria;
    private HoraDelDia horaDelDia;

    public ClaveEstadistica(Coleccion coleccion, Provincia provincia, Categoria categoria, HoraDelDia horaDelDia) {
        this.coleccion = coleccion;
        this.provincia = provincia;
        this.categoria = categoria;
        this.horaDelDia = horaDelDia;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClaveEstadistica that = (ClaveEstadistica) obj;
        return Objects.equals(coleccion, that.coleccion) &&
                Objects.equals(provincia, that.provincia) &&
                Objects.equals(categoria, that.categoria) &&
                Objects.equals(horaDelDia, that.horaDelDia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coleccion, provincia, categoria, horaDelDia);
    }
}
