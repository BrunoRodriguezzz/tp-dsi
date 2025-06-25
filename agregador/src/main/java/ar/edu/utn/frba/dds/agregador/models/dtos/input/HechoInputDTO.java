package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ContenidoMultimedia;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Etiqueta;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Ubicacion;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class HechoInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionInputDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private ContribuyenteInputDTO contribuyente;
  private String origen;
  private String fuente;
  private List<String> contenidoMultimedia;
  private List<String> etiquetas;

  public static Hecho DTOToHecho(HechoInputDTO hechoDTO, Contribuyente contribuyente, Fuente fuente) {
    Hecho hecho;
    try {
      hecho = new Hecho(
          hechoDTO.getTitulo(),
          hechoDTO.getDescripcion(),
          new Categoria(hechoDTO.getCategoria()),
          new Ubicacion(
              hechoDTO.getUbicacion().getLatitud(),
              hechoDTO.getUbicacion().getLongitud()
          ),
          hechoDTO.getFechaAcontecimiento(),
          Origen.valueOf(hechoDTO.getOrigen().toUpperCase())
      );
      hecho.setIdInternoFuente(hechoDTO.getId());
      hecho.setFuente(fuente);
      hecho.setEstaEliminado(false);
      if(contribuyente != null) {
        hecho.setContribuyente(contribuyente);
      }
      if(hechoDTO.getEtiquetas() != null) {
        hecho.setEtiquetas(hechoDTO.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toList()));
      }
      if(hechoDTO.getContenidoMultimedia() != null) {
        hecho.setContenidoMultimedia(hechoDTO.getContenidoMultimedia().stream().map(ContenidoMultimedia::new).collect(Collectors.toList()));
      }
      return hecho;
    } catch (Exception e){
      System.out.println(e.getMessage());
    }
    return null;
  }

  public static Hecho DTOToHecho(HechoInputDTO hechoDTO) {
    Hecho hecho;
    try {
      hecho = new Hecho(
          hechoDTO.getTitulo(),
          hechoDTO.getDescripcion(),
          new Categoria(hechoDTO.getCategoria()),
          new Ubicacion(
              hechoDTO.getUbicacion().getLatitud(),
              hechoDTO.getUbicacion().getLongitud()
          ),
          hechoDTO.getFechaAcontecimiento(),
          Origen.valueOf(hechoDTO.getOrigen().toUpperCase())
      );
      hecho.setIdInternoFuente(hechoDTO.getId());
      hecho.setEstaEliminado(false);
      if(hechoDTO.getContribuyente() != null) {
        hecho.setContribuyente(new Contribuyente(
            hechoDTO.getContribuyente().getNombre(),
            hechoDTO.getContribuyente().getApellido(),
            hechoDTO.getContribuyente().getFechaNacimiento()
        ));
      }
      if(hechoDTO.getEtiquetas() != null) {
        hecho.setEtiquetas(hechoDTO.getEtiquetas().stream().map(Etiqueta::new).collect(Collectors.toList()));
      }
      if(hechoDTO.getContenidoMultimedia() != null) {
        hecho.setContenidoMultimedia(hechoDTO.getContenidoMultimedia().stream().map(ContenidoMultimedia::new).collect(Collectors.toList()));
      }
      return hecho;
    } catch (Exception e){
      System.out.println(e.getMessage());
    }
    return null;
  }

  public static List<Hecho> mapDTOToHechos (List<HechoInputDTO> hechosInput) {
    return hechosInput.stream().map(HechoInputDTO::DTOToHecho).collect(Collectors.toList());
  }
}
