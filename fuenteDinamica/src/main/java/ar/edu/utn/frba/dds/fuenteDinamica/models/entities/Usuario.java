package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class Usuario {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Long idUsuario;
}