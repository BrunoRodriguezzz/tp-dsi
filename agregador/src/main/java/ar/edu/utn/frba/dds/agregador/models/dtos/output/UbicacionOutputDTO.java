package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import lombok.Data;

@Data
public class UbicacionOutputDTO {
  private String latitud;
  private String longitud;
  private String Pais;
  private String provincia;
  private String muncipio;


  public static UbicacionOutputDTO UbicacionToDTO(Ubicacion ubicacion) {
    UbicacionOutputDTO ubicacionDTO = new UbicacionOutputDTO();
    ubicacionDTO.setLatitud(String.valueOf(ubicacion.getLatitud()));
    ubicacionDTO.setLongitud(String.valueOf(ubicacion.getLongitud()));
    ubicacionDTO.setMuncipio(ubicacion.getMunicipio());
    if (ubicacion.getProvincia() != null) {
      ubicacionDTO.setProvincia(ubicacion.getProvincia().toString());
    }
    if (ubicacion.getPais() != null) {
      ubicacionDTO.setPais(ubicacion.getPais().toString());
    }
    return ubicacionDTO;
  }
}
