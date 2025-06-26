package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class FuenteInputDTO {
    private String nombre;
    private String path;
    private String tipoFuente;
    private Long idInterno;

    public static Fuente DTOToFuente(FuenteInputDTO fuenteInputDTO) {
        return new Fuente(
            fuenteInputDTO.getNombre(),
            fuenteInputDTO.getPath(),
            TipoFuente.valueOf(fuenteInputDTO.getTipoFuente()),
            fuenteInputDTO.getIdInterno()
        );
    }

    public static List<Fuente> mapDTOToFuente(List<FuenteInputDTO> fuentesInputDTO) {
        return fuentesInputDTO.stream().map(FuenteInputDTO::DTOToFuente).collect(Collectors.toList());
    }
}


