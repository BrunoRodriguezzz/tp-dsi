package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Origen;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Ubicacion;
import java.util.List;
import lombok.Data;

@Data
public class OutputHecho {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private String fechaAcontecimiento;
    private ContribuyenteDTO contribuyente;
    private Origen origen;
    private String fuente;
    private List<String> contenidoMultimedia;
    private List<String> etiquetas;
}
