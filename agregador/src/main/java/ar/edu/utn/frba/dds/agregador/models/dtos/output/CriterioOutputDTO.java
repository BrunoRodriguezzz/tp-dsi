package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import java.util.List;
import lombok.Data;

@Data
public class CriterioOutputDTO {
  List<FiltroOutputDTO> filtros;

  public static CriterioOutputDTO criterioOutputDTO(Criterio criterio) {
    CriterioOutputDTO criterioOutputDTO = new CriterioOutputDTO();

    List<FiltroOutputDTO> filtros = criterio.getFiltros().stream().map(FiltroOutputDTO::filtroToDTO).toList();
    criterioOutputDTO.setFiltros(filtros);

    return criterioOutputDTO;
  }
}
