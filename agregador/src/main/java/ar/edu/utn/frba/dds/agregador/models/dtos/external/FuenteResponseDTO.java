package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FuenteResponseDTO {
  private Long id;
  private String nombre;
  private List<HechoInputDTO> hechos;

  public static List<Hecho> servicioResponseToHechos(List<FuenteResponseDTO> servicioResponse, Fuente fuente) {
    List<Hecho> hechos = new ArrayList<>();
    servicioResponse.forEach(fuenteResponseDTO -> {
      List<Hecho> hechosEnFuente = fuenteResponseDTO
          .getHechos()
          .stream().map(h -> {
            Hecho hecho = HechoInputDTO.DTOToHecho(h, null, fuente);
            return hecho;
          })
          .toList();
      hechos.addAll(hechosEnFuente);
    });
    return hechos;
  }
}
