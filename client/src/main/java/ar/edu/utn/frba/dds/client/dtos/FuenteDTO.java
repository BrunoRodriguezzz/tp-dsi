package ar.edu.utn.frba.dds.client.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FuenteDTO {
    private String nombre;
    private Long id;
    private String tipoFuente;
    private Integer cantidadHechos;
    private LocalDateTime fechaCreacion;
}
