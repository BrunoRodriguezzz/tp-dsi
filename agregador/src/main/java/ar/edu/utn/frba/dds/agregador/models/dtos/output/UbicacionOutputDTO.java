package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Ubicacion;
import lombok.Data;

@Data
public class UbicacionOutputDTO {
  private String latitud;
  private String longitud;

  public static UbicacionOutputDTO UbicacionToDTO(Ubicacion ubicacion) {
    UbicacionOutputDTO ubicacionDTO = new UbicacionOutputDTO();
    ubicacionDTO.setLatitud(ubicacion.getLatitud());
    ubicacionDTO.setLongitud(ubicacion.getLongitud());
    return ubicacionDTO;
  }
}
