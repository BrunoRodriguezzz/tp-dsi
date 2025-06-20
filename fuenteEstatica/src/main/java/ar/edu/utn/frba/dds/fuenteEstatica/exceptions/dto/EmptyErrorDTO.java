package ar.edu.utn.frba.dds.fuenteEstatica.exceptions.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmptyErrorDTO {
    private String message;       // Descripción humana del error
    private String error;         // Tipo de error HTTP, ej: "Bad Request"
    private LocalDateTime timestamp; // Momento exacto del error
}
