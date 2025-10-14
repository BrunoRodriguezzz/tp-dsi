package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ErrorDTO {
    private String error;

}