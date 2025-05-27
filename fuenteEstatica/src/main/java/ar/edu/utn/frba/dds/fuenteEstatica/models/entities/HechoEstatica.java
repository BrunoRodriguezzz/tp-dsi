package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class HechoEstatica {
    private Long id; // Nuestro ID
    private Long idArchivo; // Es el ID de la fuente que es propietaria del hecho
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaHecho;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Boolean eliminado;
    private Origen origen;

    public void eliminar() {
        this.eliminado = true;
    }

    public void establecerUbicacion(Double lat, Double lon) {
        try {
            this.ubicacion = new Ubicacion(lat.toString(), lon.toString());
        } catch (Exception e) {} //TODO
    }

    public Boolean cumpleFecha(LocalDateTime fecha) {
        return this.fechaModificacion.isAfter(fecha) || this.fechaCreacion.isAfter(fecha);
    }
}
