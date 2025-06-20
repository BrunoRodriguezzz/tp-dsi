package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import lombok.Data;

import java.util.List;

@Data
public class OutputFuente {
    private Long id;
    private String nombre;
    private List<OutputHecho> hechos;
}
