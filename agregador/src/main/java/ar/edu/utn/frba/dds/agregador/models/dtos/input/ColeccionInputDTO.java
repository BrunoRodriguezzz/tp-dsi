package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import java.util.ArrayList;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    String nombre;
    String descripcion;
    CriterioInputDTO criterio;
    List<FuenteInputDTO> fuentes;

    public static Coleccion inputColeccionToColeccion(ColeccionInputDTO coleccionInputDTO, List<Fuente> fuentes) {
        Criterio criterio = new Criterio();
        List<Filtro> filtros = UtilsDTO.crearFiltros(coleccionInputDTO);

        if (filtros.isEmpty()) {
            throw new RuntimeException("Filtros vacios");
        }
        criterio.setFiltros(filtros);
        return new Coleccion(coleccionInputDTO.getNombre(), coleccionInputDTO.getDescripcion(), fuentes, criterio);
    }
}
