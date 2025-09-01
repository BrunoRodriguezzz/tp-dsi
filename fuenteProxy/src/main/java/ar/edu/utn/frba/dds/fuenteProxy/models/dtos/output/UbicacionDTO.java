package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UbicacionDTO {
    private String latitud;
    private String longitud;
}
