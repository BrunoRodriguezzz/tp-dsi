package entities;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private List<Etiqueta> etiquetas;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private Origen origen;
    private Contribuyente contribuyente;
    private Boolean eliminado;
    private List<ContenidoMultimedia> contenidoMultimedia;

    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.origen = origen;
        this.fechaCarga = LocalDate.now();
        this.eliminado = false;
        //TODO Usar patron builder
    }

    public Integer eliminar() {
        this.eliminado = true;
        return 1;
    }

    // Metodos
    public Boolean tieneContenidoMultimedia() {
        return !this.contenidoMultimedia.isEmpty();
    }

    public Integer serEtiquetado(Etiqueta etiqueta) throws RuntimeException {
        if (this.etiquetas.add(etiqueta) && this.etiquetas.stream().noneMatch(e -> e.coincide(etiqueta))) { //TODO Revisar porque devuelve siempre true
            return 1;
        }
        return 0;
    }
}