package ar.edu.utn.frba.dds.servicioAutenticacion.domain.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CriterioInputDTO {
  List<FiltroInputDTO> filtros;
}
