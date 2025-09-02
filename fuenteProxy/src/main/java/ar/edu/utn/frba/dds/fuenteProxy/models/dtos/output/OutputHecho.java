package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.enums.Origen;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Ubicacion;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class OutputHecho {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDate fechaAcontecimiento;
    private String fechaCarga;
    private ContribuyenteDTO contribuyente;
    private Origen origen;
    private String fuente;
    private List<String> contenidoMultimedia;
    private List<String> etiquetas;
}
