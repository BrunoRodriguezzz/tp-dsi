package ar.edu.utn.frba.dds.domain.models.entities.hechos;

import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.DescripcionInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.TituloInvalidoException;
import lombok.Getter;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.ContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Etiqueta;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@Getter
public class Hecho {
    @Setter @Getter
    private String id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private List<Etiqueta> etiquetas;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private Origen origen;
    private Contribuyente contribuyente;
    @Setter @Getter
    private Boolean estaEliminado;
    @Setter @Getter
    private String fuente;
    private List<ContenidoMultimedia> contenidoMultimedia;

    public Hecho (String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, Origen origen) throws FechaInvalidaException, TituloInvalidoException, DescripcionInvalidaException {
        if(titulo == null || titulo.isBlank()) throw new TituloInvalidoException("El título no puede estar vacío");
        if(descripcion == null || descripcion.isBlank()) throw new DescripcionInvalidaException("La descripcion no puede ser nula o vacía");
        if(fechaAcontecimiento == null) {throw new FechaInvalidaException("No se proveyó una fecha de acontecimiento");}

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        if(fechaAcontecimiento.isAfter(LocalDate.now())) {
            throw new FechaInvalidaException("La fecha: " + fechaAcontecimiento + "es una fecha futura");
        }
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.origen = origen;
        this.fechaCarga = null;
    }

    public void eliminar() throws Exception {
        if(this.estaEliminado) {throw new Exception("El hecho ya fue eliminado");}
        this.estaEliminado = true;
    }

    // Metodos
    public Boolean tieneContenidoMultimedia() {
        return !this.contenidoMultimedia.isEmpty();
    }

    public Boolean agregarEtiqueta(Etiqueta etiqueta) {
        if(this.etiquetas == null) {this.etiquetas = new ArrayList<>();}
        if (this.etiquetas.stream().noneMatch(e -> e.getTitulo().equals(etiqueta.getTitulo()))){ //TODO Revisar porque devuelve siempre true
            this.etiquetas.add(etiqueta);
            return true;
        }
        return false;
    }
}