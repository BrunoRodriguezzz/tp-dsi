package ar.edu.utn.frba.dds.client.dtos.estadisticas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaHoraXCategoriaDTO {
    private String categoria;
    private Map<LocalTime, Long> horasConHechos;
}
