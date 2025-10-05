package ar.edu.utn.frba.dds.client.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas;
    private String origen;
    private String fuente;
}
