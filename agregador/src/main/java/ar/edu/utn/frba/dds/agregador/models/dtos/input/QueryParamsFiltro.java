package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroCategoria;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaAcontecimientoFinal;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaAcontecimientoInicio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroTitulo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.RangoFechas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.springframework.cglib.core.Local;

@Setter
public class QueryParamsFiltro {
  public String categoria;
  public LocalDate fechaAcontecimientoInicio;
  public LocalDate fechaAcontecimientoFin;
  public String titulo;

  public List<Filtro> instanciarFiltros() {
    List<Filtro> filtros = new ArrayList<>();
    if(categoria != null && !categoria.isEmpty()) {
      try{
        filtros.add(new FiltroCategoria(new Categoria(categoria)));
      } catch(Exception e){throw new RuntimeException("Categoria invalida");}
    }
    if(fechaAcontecimientoInicio != null) {
      FiltroFechaAcontecimientoInicio filtroFechaAcontecimientoInicio = new FiltroFechaAcontecimientoInicio();
      filtroFechaAcontecimientoInicio.setFechaInicio(this.fechaAcontecimientoInicio);
      filtros.add(filtroFechaAcontecimientoInicio);
    }
    if (fechaAcontecimientoFin != null) {
      FiltroFechaAcontecimientoFinal filtroFechaAcontecimientoFinal = new FiltroFechaAcontecimientoFinal();
      filtroFechaAcontecimientoFinal.setFechaFinal(this.fechaAcontecimientoFin);
      filtros.add(filtroFechaAcontecimientoFinal);
    }
    if(titulo != null && !titulo.isEmpty()) {
      try{
        filtros.add(new FiltroTitulo(titulo));
      } catch(Exception e){throw new RuntimeException("Titulo invalida");}
    }
    return filtros;
  }
}
