package ar.edu.utn.frba.dds.agregador.models.domain.hechos;

import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.DescripcionInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.TituloInvalidoException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import lombok.Getter;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ContenidoMultimedia;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Etiqueta;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@Getter @Setter
public class Hecho {
    private Long id;
    private Long idInternoFuente;
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
    private Fuente fuente;
    private List<ContenidoMultimedia> contenidoMultimedia;
    private List<Consenso> consensuado;

    public Hecho (String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, Origen origen) throws FechaInvalidaException, TituloInvalidoException, DescripcionInvalidaException {
        if(titulo == null || titulo.isBlank()) throw new TituloInvalidoException("El título no puede estar vacío");
        if(descripcion == null || descripcion.isBlank()) throw new DescripcionInvalidaException("La descripcion no puede ser nula o vacía");
        if(fechaAcontecimiento == null) {throw new FechaInvalidaException("No se proveyó una fecha de acontecimiento");}

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
//        if(fechaAcontecimiento.isAfter(LocalDate.now())) {
//            throw new FechaInvalidaException("La fecha: " + fechaAcontecimiento + "es una fecha futura");
//        }
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
        if (this.etiquetas.stream().noneMatch(e -> e.getTitulo().equals(etiqueta.getTitulo()))){
            this.etiquetas.add(etiqueta);
            return true;
        }
        return false;
    }

    public boolean equals(Hecho hecho) {
        if (this == hecho) return true;
        if (hecho == null || getClass() != hecho.getClass()) return false;

        boolean resultado = this.titulo.equals(hecho.getTitulo()) &&
            this.descripcion.equals(hecho.getDescripcion()) &&
            this.categoria.getTitulo().equals(hecho.getCategoria().getTitulo()) &&
            this.ubicacion.getLatitud().equals(hecho.getUbicacion().getLatitud()) &&
            this.ubicacion.getLongitud().equals(hecho.getUbicacion().getLongitud()) &&
            this.fechaAcontecimiento.equals(hecho.fechaAcontecimiento) &&
            this.id.equals(hecho.getId()) &&
            this.idInternoFuente.equals(hecho.getIdInternoFuente()) &&
            this.origen.equals(hecho.getOrigen()) &&
            this.fuente.equals(hecho.getFuente());
        return resultado;
    }
}