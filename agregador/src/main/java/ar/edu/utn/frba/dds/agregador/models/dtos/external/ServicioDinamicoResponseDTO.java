package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import java.util.List;
import lombok.Data;

@Data
//Servicio Proxy, servicio Dinámico, servicio Estático
public class ServicioDinamicoResponseDTO {
  private List<HechoServicioResponseDTO> hechos;
}
