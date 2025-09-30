package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import lombok.Data;

@Data
public class UbicacionOutputGobiernoDTO {
    private Double lat;
    private Double lon;
    private boolean aplanar = true;
    private String campos = "estandar";

    public UbicacionOutputGobiernoDTO(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
