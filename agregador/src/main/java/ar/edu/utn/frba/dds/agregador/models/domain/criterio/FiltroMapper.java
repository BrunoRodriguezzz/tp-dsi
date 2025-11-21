package ar.edu.utn.frba.dds.agregador.models.domain.criterio;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.*;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FiltroMapper {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    public List<EntidadFiltro> toEntities(List<Filtro> filtros) {
        return filtros.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public EntidadFiltro toEntity(Filtro filtro) {
        if (filtro instanceof FiltroCategoria f) {
            String tituloCategoria = f.getCategoria().getTitulo();
            // Busca categoría existente o la crea si no existe
            Categoria categoriaExistente = categoriaRepository.findByTitulo(tituloCategoria)
                    .orElseGet(() -> {
                        Categoria nuevaCategoria = new Categoria(tituloCategoria);
                        return categoriaRepository.save(nuevaCategoria);
                    });
            return new FiltroCategoria(categoriaExistente);
        }
        if (filtro instanceof FiltroEtiqueta f) {
            return new FiltroEtiqueta(f.getEtiqueta());
        }
        if (filtro instanceof FiltroTitulo f) {
            return new FiltroTitulo(f.getTitulo());
        }
        if (filtro instanceof FiltroLatitud f) {
            return new FiltroLatitud(f.getLatitud());
        }
        if (filtro instanceof FiltroLongitud f) {
            return new FiltroLongitud(f.getLongitud());
        }
        if (filtro instanceof FiltroFechaCargaInicio f) {
            return new FiltroFechaCargaInicio(f.getFechaInicio());
        }
        if (filtro instanceof FiltroFechaCargaFinal f) {
            return new FiltroFechaCargaFinal(f.getFechaFinal());
        }
        if (filtro instanceof FiltroFechaAcontecimientoInicio f) {
            return new FiltroFechaAcontecimientoInicio(f.getFechaInicio());
        }
        if (filtro instanceof FiltroFechaAcontecimientoFinal f) {
            return new FiltroFechaAcontecimientoFinal(f.getFechaFinal());
        }

        throw new IllegalArgumentException("Tipo de filtro no soportado: " + filtro.getClass().getSimpleName());
    }
}