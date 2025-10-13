package ar.edu.utn.frba.dds.client.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CriterioDTO {
    List<FiltroDTO> filtros;
}
