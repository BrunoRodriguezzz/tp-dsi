package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;

import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private String nombre;
    private String descripcion;
    private CriterioInputDTO criterio;
    private List<NombreFuenteInputDTO> fuentes;
    private List<String> consensos;

    public static Coleccion inputColeccionToColeccion(ColeccionInputDTO coleccionInputDTO, List<Fuente> fuentes) {
        Criterio criterio = new Criterio();

        // 1) crear filtros (sigue devolviendo List<Filtro>)
        List<Filtro> filtros = CriterioInputDTO.crearFiltros(coleccionInputDTO.getCriterio());

        // 2) convertir **seguro** a List<EntidadFiltro>
        List<EntidadFiltro> filtrosEntidad = filtros.stream()
                .map(f -> {
                    if (f instanceof EntidadFiltro) {
                        return (EntidadFiltro) f;
                    } else {
                        // Si llegara a pasar, fallamos rápido para detectar el problema (puedes cambiar esto por un mapper)
                        throw new IllegalStateException("El filtro creado NO es una EntidadFiltro: " + f.getClass().getName());
                    }
                })
                .collect(Collectors.toList());

        // 3) establecer la relación bidireccional y añadir al criterio
        // mejor usar un setter especial en criterio (recomendado) ó setCriterio en cada filtro:
        filtrosEntidad.forEach(f -> f.setCriterio(criterio));
        criterio.getFiltros().addAll(filtrosEntidad);

        // procesar consensos
        List<Consenso> listaConsensos = new ArrayList<>();
        if (coleccionInputDTO.getConsensos() != null) {
            listaConsensos = coleccionInputDTO.getConsensos()
                    .stream()
                    .map(Consenso::valueOf)
                    .collect(Collectors.toList());
        }

        Coleccion coleccion = new Coleccion(
                coleccionInputDTO.getNombre(),
                coleccionInputDTO.getDescripcion(),
                fuentes,
                criterio
        );

        listaConsensos.forEach(coleccion::agregarConsenso);

        return coleccion;
    }
}
