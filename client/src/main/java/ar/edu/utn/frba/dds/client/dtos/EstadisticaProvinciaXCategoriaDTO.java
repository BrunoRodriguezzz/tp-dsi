package ar.edu.utn.frba.dds.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaProvinciaXCategoriaDTO {

    private String categoria;
    private Map<String, Long> provinciasConHechos;

}
