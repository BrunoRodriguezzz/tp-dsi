package ar.edu.utn.frba.dds.client.dtos;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.solicitud.ResolucionDTO;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SolicitudEliminacionConHechoDTO {
  private Long id;
  private HechoDTO hecho;
  private String fundamento;
  private LocalDateTime fechaCreacion;
  private Long contribuyente;
  private String estado;
  private ResolucionDTO resolucion;
}
