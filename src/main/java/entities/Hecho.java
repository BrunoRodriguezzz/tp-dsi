package entities;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private String ubicacion;
    private List<Etiqueta> etiquetas;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private Origen origen;
    private Contribuyente contribuyente;
    private Boolean eliminado;
    private List<ContenidoMultimedia> contenidoMultimedia;

    public Integer eliminar() {
        this.eliminado = true;
        return 1;
    }

    // Metodos
    public Boolean tieneContenidoMultimedia() {
        return !this.contenidoMultimedia.isEmpty();
    }

    public Integer serEtiquetado(Etiqueta etiqueta) throws RuntimeException {
        if (this.etiquetas.stream().noneMatch(e -> e.coincide(etiqueta)) && this.etiquetas.add(etiqueta)) { //TODO Revisar porque devuelve siempre true
            return 1;
        }
        return 0;
    }

    public Boolean estaEliminado(){
        return this.eliminado;
    }
}