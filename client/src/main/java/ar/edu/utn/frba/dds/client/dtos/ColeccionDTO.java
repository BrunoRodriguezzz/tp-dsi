package ar.edu.utn.frba.dds.client.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private long cantidadHechos;
    private CriterioDTO criterio;
    private List<String> fuentes;
    private List<String> consensos;
}
