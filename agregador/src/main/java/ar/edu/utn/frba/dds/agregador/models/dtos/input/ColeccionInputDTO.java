package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;

import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    String nombre;
    String descripcion;
    CriterioInputDTO criterio;
    List<NombreFuenteInputDTO> fuentes;
    List<String> consensos;

    public static Coleccion inputColeccionToColeccion(ColeccionInputDTO coleccionInputDTO, List<Fuente> fuentes) {
        Criterio criterio = new Criterio();
        List<Filtro> filtros = CriterioInputDTO.crearFiltros(coleccionInputDTO.getCriterio());
        List<Consenso> listaConsensos = new ArrayList<>();
        if (coleccionInputDTO.getConsensos() != null) {
            listaConsensos = coleccionInputDTO.getConsensos().stream().map(Consenso::valueOf).collect(Collectors.toList());
        }
        criterio.setFiltros(filtros);
        Coleccion coleccion = new Coleccion(coleccionInputDTO.getNombre(), coleccionInputDTO.getDescripcion(), fuentes, criterio);
        listaConsensos.forEach(coleccion::agregarConsenso);
        return coleccion;
    }

}
