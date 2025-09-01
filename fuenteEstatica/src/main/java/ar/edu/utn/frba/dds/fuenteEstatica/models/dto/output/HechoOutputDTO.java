package ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output;


import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Fuente;
import ar.edu.utn.frba.dds.fuenteEstatica.models.enums.Origen;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Ubicacion;
import java.util.List;
import lombok.Data;

@Data
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private String fechaAcontecimiento;
    private String fechaCarga;
    private Contribuyente contribuyente;
    private Fuente fuente;
    private Origen origen;
    private List<String> contenidoMultimedia;
    private List<String> etiquetas;
}
