package ar.edu.utn.frba.dds.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaSolicitudesDTO {
    private LocalDateTime fecha;
    private Long solicitudes_spam;
    private Long solicitudes_no_spam;
}
