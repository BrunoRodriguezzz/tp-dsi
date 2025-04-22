package models.entities.hechos;

import lombok.Getter;
import models.entities.ContenidoMultimedia;
import models.entities.enums.Origen;
import models.entities.filtros.Categoria;
import models.entities.filtros.Etiqueta;
import models.entities.filtros.Ubicacion;
import models.entities.usuarios.Contribuyente;

import java.time.LocalDate;
import java.util.ArrayList;
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
        this.etiquetas = new ArrayList<>();
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

    public Integer agregarEtiqueta(Etiqueta etiqueta) throws RuntimeException {
        if (this.etiquetas.add(etiqueta) && this.etiquetas.stream().noneMatch(e -> e.getTitulo().equals(etiqueta.getTitulo()))){ //TODO Revisar porque devuelve siempre true
            return 1;
        }
        return 0;
    }
}