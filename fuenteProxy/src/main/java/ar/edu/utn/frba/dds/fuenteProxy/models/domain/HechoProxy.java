package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HechoProxy {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private Date fechaHecho;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private Long idFuente;
    private Boolean eliminado;

    public void eliminar() {
        this.eliminado = true;
    }

    public Boolean cumpleFecha(Date fecha) {
        return this.fechaModificacion.after(fecha) || this.fechaCreacion.after(fecha);
    }
}
