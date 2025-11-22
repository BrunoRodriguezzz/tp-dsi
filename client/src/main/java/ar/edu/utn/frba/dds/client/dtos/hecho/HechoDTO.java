package ar.edu.utn.frba.dds.client.dtos.hecho;
import ar.edu.utn.frba.dds.client.dtos.ContribuyenteDTO;
import ar.edu.utn.frba.dds.client.dtos.UbicacionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private ContribuyenteDTO contribuyente;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private List<String> etiquetas;
    private List<String> contenidoMultimedia;
    private String estado;
    private String origen;
    private String fuente;
    private Boolean verificado;
    @JsonProperty(required = false)
    private String sugerenciaCambio;
}