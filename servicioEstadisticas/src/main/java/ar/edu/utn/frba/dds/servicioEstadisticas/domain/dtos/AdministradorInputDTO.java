package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class AdministradorInputDTO {
  private String nombre;
  private String apellido;
}
