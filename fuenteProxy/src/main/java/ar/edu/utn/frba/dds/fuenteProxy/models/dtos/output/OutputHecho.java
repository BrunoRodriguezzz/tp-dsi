package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.util.List;
import lombok.Data;

@Data
public class OutputHecho {
    private Long id;
    private ContribuyenteDTO contribuyente;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<String> contenidoMultimedia;
    private Ubicacion ubicacion;
    private String fechaAcontecimiento;
    private List<String> etiquetas;
    private Origen origen;
    private String fuente;
}
