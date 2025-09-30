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
public class HechoOutputDTO {
    private String titulo;
    private String categoria;
    private UbicacionOutputDTO ubicacion;
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas;
}
