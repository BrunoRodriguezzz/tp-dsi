package ar.edu.utn.frba.dds.client.dtos;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SolicitudEliminacionDTO {
  private Long idHecho;
  private String fundamento;
  private LocalDateTime fechaCreacion;
  private Long idContribuyente;
}
