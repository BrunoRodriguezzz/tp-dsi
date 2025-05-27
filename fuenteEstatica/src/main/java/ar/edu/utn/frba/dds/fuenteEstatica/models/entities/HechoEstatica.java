package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class HechoEstatica {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaHecho;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long idArchivo;
    private Boolean eliminado;
    private Origen origen;

    public void eliminar() {
        this.eliminado = true;
    }

    public Boolean cumpleFecha(LocalDateTime fecha) {
        return this.fechaModificacion.isAfter(fecha) || this.fechaCreacion.isAfter(fecha);
    }
}
