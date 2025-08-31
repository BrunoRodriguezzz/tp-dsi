package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ColeccionInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private List<HechoInputDTO> hechos;
  private CriterioInputDTO criterio;
  private List<String> fuentes;
  private List<String> consensos;
}
