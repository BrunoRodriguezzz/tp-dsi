package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class HechoEstatica {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaHecho;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long idFuente;
    private Boolean eliminado;
    private Origen origen;

    public HechoEstatica(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public void eliminar() {
        this.eliminado = true;
    }

    public Boolean cumpleFecha(LocalDateTime fecha) {
        return this.fechaModificacion.isAfter(fecha) || this.fechaCreacion.isAfter(fecha);
    }
}
