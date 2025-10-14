package ar.edu.utn.frba.dds.client.dtos.estadisticas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaCategoriaDTO {
    private Map<String, Long> categoriasConHechos;
}
