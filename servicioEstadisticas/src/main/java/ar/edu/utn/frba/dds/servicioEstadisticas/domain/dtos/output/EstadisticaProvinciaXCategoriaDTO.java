package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaProvinciaXCategoriaDTO {
    private String categoria;
    private Map<String, Long> provinciasConHechos;
}