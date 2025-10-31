package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaCategoriaDTO {
    private Map<String, Long> categoriasConMasHechos;
}
