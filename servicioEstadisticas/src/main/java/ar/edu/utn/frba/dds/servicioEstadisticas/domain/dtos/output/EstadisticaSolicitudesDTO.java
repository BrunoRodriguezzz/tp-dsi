package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaSolicitudesDTO {
    private Long solicitudes_spam;
    private Long solicitudes_no_spam;
}
