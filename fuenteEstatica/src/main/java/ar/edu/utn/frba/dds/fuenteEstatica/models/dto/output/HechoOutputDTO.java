package ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import lombok.Data;

@Data
public class HechoOutputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private String fecha_hecho;
    private String created_at;
    private String updated_at;
    private Long id_hecho;
    private Origen origen;
}
