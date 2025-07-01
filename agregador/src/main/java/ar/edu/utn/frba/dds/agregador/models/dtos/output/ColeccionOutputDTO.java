package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;

import java.util.ArrayList;
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
  private List<String> fuentes;

  public static ColeccionOutputDTO coleccionToDTO(Coleccion coleccion) {
    ColeccionOutputDTO coleccionDTO = new ColeccionOutputDTO();
    coleccionDTO.setId(coleccion.getId());
    coleccionDTO.setTitulo(coleccion.getTitulo());
    coleccionDTO.setDescripcion(coleccion.getDescripcion());
    coleccionDTO.setHechos(HechoOutputDTO.mapHechoToDTO(coleccion.getHechos()));
    coleccionDTO.setCriterio(CriterioOutputDTO.criterioOutputDTO(coleccion.getCriterio()));

    List<String> nombreFuentes = new ArrayList<>();
    coleccion.getFuentes().forEach(f -> nombreFuentes.add(f.getNombre()));
    coleccionDTO.fuentes = nombreFuentes;

    return coleccionDTO;
  }

  public static List<ColeccionOutputDTO> mapColeccionesToDTO(List<Coleccion> colecciones) {
    List<ColeccionOutputDTO> coleccionesDTO = colecciones.stream().map(ColeccionOutputDTO::coleccionToDTO).collect(Collectors.toList());
    return coleccionesDTO;
  }
}
