package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.RangoFechasInputDTO;
import java.time.LocalDateTime;
import java.util.List;

public class CriterioOutputDTO {
  String titulo;
  LocalDateTime fechaInicio;
  LocalDateTime fechaFin;
  String categoria;

  public static CriterioOutputDTO criterioOutputDTO(Criterio criterio) {
    List<Filtro> filtros = criterio.getFiltros();

  }
}
