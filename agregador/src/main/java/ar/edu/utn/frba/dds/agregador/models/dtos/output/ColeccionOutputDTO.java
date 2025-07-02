package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class ColeccionOutputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private List<HechoOutputDTO> hechos;
  private CriterioOutputDTO criterio;

  public static ColeccionOutputDTO coleccionToDTO(Coleccion coleccion) {
    ColeccionOutputDTO coleccionDTO = new ColeccionOutputDTO();
    coleccionDTO.setId(coleccion.getId());
    coleccionDTO.setTitulo(coleccion.getTitulo());
    coleccionDTO.setDescripcion(coleccion.getDescripcion());
    coleccionDTO.setHechos(HechoOutputDTO.mapHechoToDTO(coleccion.getHechos()));
    coleccionDTO.setCriterio(CriterioOutputDTO.criterioOutputDTO(coleccion.getCriterio()));
    return coleccionDTO;
  }

  public static List<ColeccionOutputDTO> mapColeccionesToDTO(List<Coleccion> colecciones) {
    List<ColeccionOutputDTO> coleccionesDTO = colecciones.stream().map(ColeccionOutputDTO::coleccionToDTO).collect(Collectors.toList());
    return coleccionesDTO;
  }
}
