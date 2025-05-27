package ar.edu.utn.frba.dds.fuenteEstatica.models.dto;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import lombok.Data;

@Data
public class HechoOutputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private String fechaHecho;
    private String createdAt;
    private String updatedAt;
    private Long idFuente;
    private Long idHecho;
    private Origen origen;
}
