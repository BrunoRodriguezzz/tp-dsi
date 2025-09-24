package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class HechoOutputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionOutputDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private ContribuyenteOutputDTO contribuyente;
  private String fuente;
  private String origen;
  private LocalDate fechaCarga;

  public static HechoOutputDTO HechoToDTO(Hecho hecho) {
    HechoOutputDTO hechoDTO = new HechoOutputDTO();
    hechoDTO.setId(hecho.getId());
    hechoDTO.setTitulo(hecho.getTitulo());
    hechoDTO.setDescripcion(hecho.getDescripcion());
    hechoDTO.setCategoria(hecho.getCategoria().getTitulo());
    hechoDTO.setUbicacion(UbicacionOutputDTO.UbicacionToDTO(hecho.getUbicacion()));
    hechoDTO.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    hechoDTO.setFechaCarga(hecho.getFechaCarga());

    if(hecho.getContribuyente() != null){
      hechoDTO.setContribuyente(ContribuyenteOutputDTO.ContribuyenteToDTO(hecho.getContribuyente()));
    }
    else{
      hechoDTO.setContribuyente(null);
    }

    hecho.getFuenteSet()
            .stream()
            .findFirst()
            .ifPresent(f -> hechoDTO.setFuente(f.getFuente().getNombre()));
    hechoDTO.setOrigen(hecho.getOrigen().name());
    return hechoDTO;
  }

  public static List<HechoOutputDTO> mapHechoToDTO(List<Hecho> hechos) {
    if(hechos == null){
      return null;
    }
      return hechos.stream()
          .map(HechoOutputDTO::HechoToDTO)
          .collect(Collectors.toList());
  }
}
