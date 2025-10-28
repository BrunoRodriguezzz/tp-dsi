package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class EstadisticaHoraXCategoriaDTO {
    private String categoria;
    private Map<LocalTime, Long> horasConHechos;
}
