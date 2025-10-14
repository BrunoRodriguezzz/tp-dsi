package ar.edu.utn.frba.dds.client.dtos.solicitud;

import ar.edu.utn.frba.dds.client.dtos.ContribuyenteDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudDTO {
    private Long id;
    private HechoDTO hecho;
    private String fundamento;
    private LocalDateTime fechaCreacion;
    private ContribuyenteDTO contribuyente;
    private String estado;
    private ResolucionDTO resolucion;
}
