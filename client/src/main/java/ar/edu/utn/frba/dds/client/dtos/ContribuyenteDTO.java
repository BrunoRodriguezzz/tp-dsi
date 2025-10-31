package ar.edu.utn.frba.dds.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContribuyenteDTO {
    private String nombre;
    private String apellido;
    private LocalDateTime fechaNacimiento;
}
