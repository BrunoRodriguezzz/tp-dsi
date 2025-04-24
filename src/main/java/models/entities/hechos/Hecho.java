package models.entities.hechos;

import lombok.Getter;
import models.entities.utils.Errores.ER_ValueObjects.DescripcionInvalidaException;
import models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import models.entities.utils.Errores.ER_ValueObjects.TituloInvalidoException;
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
    private Boolean estaEliminado;
    private List<ContenidoMultimedia> contenidoMultimedia;

    public Hecho (String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, Origen origen) throws FechaInvalidaException, TituloInvalidoException, DescripcionInvalidaException {
        if(titulo == null || titulo.isBlank()) throw new TituloInvalidoException("El título no puede estar vacío");
        this.titulo = titulo;
        if(descripcion == null || descripcion.isBlank()) throw new DescripcionInvalidaException("La descripcion no puede ser nula o vacía");
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        if(fechaAcontecimiento == null || fechaAcontecimiento.isAfter(LocalDate.now())) {
            throw new FechaInvalidaException("La fecha: " + fechaAcontecimiento + "es una fecha futura");
        }
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.origen = origen;
        this.fechaCarga = LocalDate.now();
        this.estaEliminado = false;
        this.etiquetas = new ArrayList<>();
    }

    public void eliminar() {
        this.estaEliminado = true;
    }

    // Metodos
    public Boolean tieneContenidoMultimedia() {
        return !this.contenidoMultimedia.isEmpty();
    }

    public Boolean agregarEtiqueta(Etiqueta etiqueta) {
        if (this.etiquetas.stream().noneMatch(e -> e.getTitulo().equals(etiqueta.getTitulo()))){ //TODO Revisar porque devuelve siempre true
            this.etiquetas.add(etiqueta);
            return true;
        }
        return false;
    }
}