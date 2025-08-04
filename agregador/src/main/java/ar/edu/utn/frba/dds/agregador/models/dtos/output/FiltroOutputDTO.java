package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import lombok.Data;

@Data
public class FiltroOutputDTO {
  private String nombre;
  private String detalle;

  public static FiltroOutputDTO filtroToDTO(Filtro filtro) {
    FiltroOutputDTO filtroOutputDTO = new FiltroOutputDTO();
    filtroOutputDTO.setNombre(filtro.getClass().getSimpleName());
    filtroOutputDTO.setDetalle(filtro.toDTO());
    return filtroOutputDTO;
  }
}

