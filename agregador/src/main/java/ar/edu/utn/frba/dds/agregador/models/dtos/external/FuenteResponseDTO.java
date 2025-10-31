package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;

@Data
public class FuenteResponseDTO {
  private static final Logger log = LoggerFactory.getLogger(FuenteResponseDTO.class);

  private Long id;
  private String nombre;
  private List<HechoInputDTO> hechos;

  public static List<Hecho> servicioResponseToHechos(List<FuenteResponseDTO> servicioResponse, Fuente fuente) {
    List<Hecho> hechos = new ArrayList<>();
    servicioResponse.forEach(fuenteResponseDTO -> {
      List<Hecho> hechosEnFuente = fuenteResponseDTO
          .getHechos()
          .stream()
          .map(h -> HechoInputDTO.DTOToHecho(h, null, fuente))
          .filter(h -> h != null)
          .collect(Collectors.toList());
      if (hechosEnFuente.size() != fuenteResponseDTO.getHechos().size()) {
        log.warn("Se omitieron {} hechos nulos al convertir la respuesta de la fuente '{}' (idInterno={}).",
            fuenteResponseDTO.getHechos().size() - hechosEnFuente.size(), fuente.getNombre(), fuente.getIdInternoFuente());
      }
      hechos.addAll(hechosEnFuente);
    });
    return hechos;
  }

  public static List<Hecho> toHechos(FuenteResponseDTO response, Fuente fuente) {
      List<Hecho> hechos = response
              .getHechos()
              .stream()
              .map(hechoInputDTO -> HechoInputDTO.DTOToHecho(hechoInputDTO, null, fuente))
              .filter(h -> h != null)
              .collect(Collectors.toList());
      if (hechos.size() != response.getHechos().size()) {
          log.warn("Se omitieron {} hechos nulos al convertir la respuesta de la fuente '{}' (idInterno={}).",
              response.getHechos().size() - hechos.size(), fuente.getNombre(), fuente.getIdInternoFuente());
      }
      return hechos;
  }
}
