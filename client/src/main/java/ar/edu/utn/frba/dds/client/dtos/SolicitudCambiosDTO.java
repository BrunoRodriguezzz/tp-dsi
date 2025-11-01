package ar.edu.utn.frba.dds.client.dtos;

import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCambiosDTO {
    private SolicitudModOutputDTO solicitud;
    private HechoDTO hechoOriginal;
}
