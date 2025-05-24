package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Fuente {
    private TipoFuente tipoFuente;
    private Long id;
    private String nombre;
    private String ruta;
    private String apiKey;

    public List<HechoDTO> getAllHechos() {
        //TODO
        return null;
    }
}
