package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import lombok.Data;

@Data
public class FuenteOutputDTO {
    private String nombre;
    private Long id;
    private String tipoFuente;
    private Integer cantidadHechos;

    public static FuenteOutputDTO toOutputDTO(Fuente fuente, Integer cantidadHechos) {
        FuenteOutputDTO dto = new FuenteOutputDTO();
        dto.setNombre(fuente.getNombre());
        dto.setId(fuente.getId());
        dto.setTipoFuente(fuente.getTipoFuente().toString());
        dto.setCantidadHechos(cantidadHechos);
        return dto;
    }
}
