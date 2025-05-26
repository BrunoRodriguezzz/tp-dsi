package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class Contribuyente {
    private String      nombre;
    private LocalDate   fechaDeNacimiento;
    private Long        idContribuyente;
}
