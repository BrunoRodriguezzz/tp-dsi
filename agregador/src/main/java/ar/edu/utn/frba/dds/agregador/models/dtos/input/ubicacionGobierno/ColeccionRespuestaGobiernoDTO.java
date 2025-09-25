package ar.edu.utn.frba.dds.agregador.models.dtos.input.ubicacionGobierno;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionRespuestaGobiernoDTO {
    private List<ResponseUbicacionGobierno> resultados;
}
