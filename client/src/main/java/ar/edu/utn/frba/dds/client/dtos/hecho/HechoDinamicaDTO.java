package ar.edu.utn.frba.dds.client.dtos.hecho;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HechoDinamicaDTO {
    private Long idAdministrador;
    private Long id;
    private List<String> etiquetas;
    private String estadoHecho;
    private String sugerenciaDeCambio;
}
