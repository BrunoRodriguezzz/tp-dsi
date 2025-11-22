package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Set;

import lombok.Data;

@Data
public class ColeccionOutputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private long cantidadHechos;
  private long cantidadHechosCurados;
  private CriterioOutputDTO criterio;
  private List<String> fuentes;
  private List<String> consensos;

  public static ColeccionOutputDTO coleccionToDTO(Coleccion coleccion) {
    ColeccionOutputDTO coleccionDTO = new ColeccionOutputDTO();
    coleccionDTO.setId(coleccion.getId());
    coleccionDTO.setTitulo(coleccion.getTitulo());
    coleccionDTO.setDescripcion(coleccion.getDescripcion());
    coleccionDTO.setCriterio(CriterioOutputDTO.criterioOutputDTO(coleccion.getCriterio()));
    coleccionDTO.setConsensos(coleccion.getConsensos().stream().map(Enum::toString).collect(Collectors.toList()));

    long cantidadUnica = 0;
    long cantidadCurados = 0;
    if (coleccion.getHechos() != null) {
      Set<Long> ids = coleccion.getHechos().stream()
              .map(h -> h.getId())
              .filter(Objects::nonNull)
              .collect(Collectors.toSet());
      long nullIdCount = coleccion.getHechos().stream()
              .filter(h -> h.getId() == null)
              .map(System::identityHashCode)
              .distinct()
              .count();
      cantidadUnica = ids.size() + nullIdCount;

      // Si la colección no tiene consensos, todos los hechos son "curados"
      if (coleccion.getConsensos() == null || coleccion.getConsensos().isEmpty()) {
        cantidadCurados = cantidadUnica;
      } else {
        // Si la colección tiene consensos, contar solo hechos que tienen TODOS los consensos requeridos
        cantidadCurados = coleccion.getHechos().stream()
                .filter(h -> h.getConsensos() != null && !h.getConsensos().isEmpty())
                .filter(h -> coleccion.getConsensos().stream()
                    .allMatch(consensoRequerido -> h.getConsensos().contains(consensoRequerido)))
                .map(h -> h.getId())
                .filter(Objects::nonNull)
                .distinct()
                .count();
      }
    }
    coleccionDTO.setCantidadHechos(cantidadUnica);
    coleccionDTO.setCantidadHechosCurados(cantidadCurados);

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
