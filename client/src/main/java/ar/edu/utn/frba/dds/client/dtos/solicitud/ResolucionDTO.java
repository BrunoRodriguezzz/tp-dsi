package ar.edu.utn.frba.dds.client.dtos.solicitud;

import ar.edu.utn.frba.dds.client.dtos.ContribuyenteDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResolucionDTO {
    private ContribuyenteDTO administrador;
    private LocalDateTime fechaResolucion;
}
