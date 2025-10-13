package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import lombok.Data;

@Data
public class FuenteOutputDTO {
    private String nombre;
    private Long id;
    private String tipoFuente;

    public static FuenteOutputDTO toOutputDTO(Fuente fuente) {
        FuenteOutputDTO dto = new FuenteOutputDTO();
        dto.setNombre(fuente.getNombre());
        dto.setId(fuente.getId());
        dto.setTipoFuente(fuente.getTipoFuente().toString());
        return dto;
    }
}
