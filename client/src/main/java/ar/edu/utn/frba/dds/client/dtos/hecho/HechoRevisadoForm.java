package ar.edu.utn.frba.dds.client.dtos.hecho;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HechoRevisadoForm {
    @NotNull
    private Long idAdministrador;

    @NotNull
    private Long id;
    private String etiquetas;

    @NotNull
    private String estadoHecho;
    private String sugerenciaDeCambio;
}
