package ar.edu.utn.frba.dds.fuenteProxy.models.dtos;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import lombok.Data;

@Data
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String fecha_hecho;
    private String created_at;
    private String updated_at;
    private Long id_fuente;
    private Long id_hecho;
    private Origen origen;
}
