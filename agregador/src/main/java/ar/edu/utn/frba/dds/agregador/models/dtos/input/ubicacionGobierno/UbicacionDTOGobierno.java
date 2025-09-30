package ar.edu.utn.frba.dds.agregador.models.dtos.input.ubicacionGobierno;

import lombok.Data;

@Data
public class UbicacionDTOGobierno {
    private String provincia_nombre;
    private String municipio_nombre;
    private String lat;
    private String lon;
}
