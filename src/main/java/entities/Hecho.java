package entities;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

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
    private List<String> imagenes;
    private List<String> audios;
    private List<String> videos;

    public Integer eliminar(){
        this.eliminado = true;
        return 1;
    }


    // Metodos
    public Boolean tieneContenidoMultimedia(){
        return true; // TODO Agregar contenido multimedia Clase + Completar este método
    }
}
