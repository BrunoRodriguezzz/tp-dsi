package models.entities.hechos;

import lombok.Getter;
import models.entities.valueObjectsHecho.ContenidoMultimedia;
import models.entities.valueObjectsHecho.Origen;
import models.entities.valueObjectsHecho.Categoria;
import models.entities.valueObjectsHecho.Etiqueta;
import models.entities.valueObjectsHecho.Ubicacion;
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

    public Hecho (String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, Origen origen) {
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

    public void eliminar() {
        this.eliminado = true;
    }

    // Metodos
    public Boolean tieneContenidoMultimedia() {
        return !this.contenidoMultimedia.isEmpty();
    }

    public Boolean agregarEtiqueta(Etiqueta etiqueta) throws RuntimeException {
        if (this.etiquetas.stream().noneMatch(e -> e.getTitulo().equals(etiqueta.getTitulo()))){ //TODO Revisar porque devuelve siempre true
            this.etiquetas.add(etiqueta);
            return true;
        }
        return false;
    }
}