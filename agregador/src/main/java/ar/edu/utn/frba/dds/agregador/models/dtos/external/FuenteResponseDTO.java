package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FuenteResponseDTO {
  private Long id;
  private String nombre;
  private List<HechoInputDTO> hechos;

  public static List<Hecho> servicioResponseToHechos(List<FuenteResponseDTO> servicioResponse) {
    List<Hecho> hechos = new ArrayList<>();
    servicioResponse.forEach(fuente -> {
      List<Hecho> hechosEnFuente = fuente
          .getHechos()
          .stream().map(h -> {
            Hecho hecho = HechoInputDTO.DTOToHecho(h);
            return hecho;
          })
          .toList();
      hechos.addAll(hechosEnFuente);
    });
    return hechos;
  }
}
