package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UbicacionesOutputGobiernoDTO {
    private List<UbicacionOutputGobiernoDTO> ubicaciones;

    public UbicacionesOutputGobiernoDTO() {
        this.ubicaciones = new ArrayList<UbicacionOutputGobiernoDTO>();
    }

    public void agregarUbicacion(UbicacionOutputGobiernoDTO ubicacion) {
        this.ubicaciones.add(ubicacion);
    }

    public void agregarUbicacion(Ubicacion ubicacion) {
        Double lat = Double.parseDouble(ubicacion.getLatitud());
        Double lon = Double.parseDouble(ubicacion.getLongitud());
        UbicacionOutputGobiernoDTO dto = new UbicacionOutputGobiernoDTO(lat, lon);
        this.ubicaciones.add(dto);
    }
}
