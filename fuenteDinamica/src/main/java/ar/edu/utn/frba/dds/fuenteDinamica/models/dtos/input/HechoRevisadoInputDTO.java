package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Etiqueta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter

public class HechoRevisadoInputDTO {

    private Long         idAdministrador;
    private Long         id;
    private List<String> etiquetas;
    private EstadoHecho  estadoHecho;
    private String       sugerenciaDeCambio;

}
