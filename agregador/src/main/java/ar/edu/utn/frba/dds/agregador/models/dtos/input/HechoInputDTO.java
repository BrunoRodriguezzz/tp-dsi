package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ContenidoMultimedia;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Etiqueta;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class HechoInputDTO {
  private static final Logger log = LoggerFactory.getLogger(HechoInputDTO.class);

  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionInputDTO ubicacion;
  private LocalDateTime fechaAcontecimiento;
  private ContribuyenteInputDTO contribuyente;
  private String origen;
  private String fuente;
  private List<String> contenidoMultimedia;
  private List<String> etiquetas;
  private LocalDateTime fechaCarga;

  public static Hecho DTOToHecho(HechoInputDTO hechoDTO, Contribuyente contribuyente, Fuente fuente) {
    Hecho hecho;
    try {
      hecho = new Hecho(
          hechoDTO.getTitulo(),
          hechoDTO.getDescripcion(),
          new Categoria(hechoDTO.getCategoria()),
          new Ubicacion(
              Double.parseDouble(hechoDTO.getUbicacion().getLatitud()),
              Double.parseDouble(hechoDTO.getUbicacion().getLongitud())
          ),
          hechoDTO.getFechaAcontecimiento(),
          Origen.valueOf(hechoDTO.getOrigen().toUpperCase())
      );
      hecho.agregarFuente(fuente, hechoDTO.getId());
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

      if(hechoDTO.getFechaCarga() != null) {
        hecho.setFechaCarga(hechoDTO.getFechaCarga());
      }

      return hecho;
    } catch (Exception e){
      log.error("Error convirtiendo HechoInputDTO a Hecho: {}", e.getMessage(), e);
      return null;
    }
  }

  public static List<Hecho> mapDTOToHechos (List<HechoInputDTO> hechosInput, Fuente fuente) {
    return hechosInput.stream().map(h -> HechoInputDTO.DTOToHecho(h, null, fuente)).collect(Collectors.toList());
  }
}
