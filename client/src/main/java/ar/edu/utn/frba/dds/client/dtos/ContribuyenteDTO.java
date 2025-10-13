package ar.edu.utn.frba.dds.client.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContribuyenteDTO {
    private String nombre;
    private String apellido;
    private LocalDateTime fechaNacimiento;
}
