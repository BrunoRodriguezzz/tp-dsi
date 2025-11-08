package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class CriterioInputDTO {
  List<FiltroInputDTO> filtros;
}
