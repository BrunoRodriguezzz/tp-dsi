package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.FiltroCategoria;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.FiltroFechaAcontecimiento;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.FiltroTitulo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.RangoFechas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@Setter
public class QueryParamsFiltro {
  public String categoria;
  public String fechaInicio;
  public String fechaFin;
  public String titulo;

  public List<Filtro> instanciarFiltros() {
    List<Filtro> filtros = new ArrayList<>();
    if(categoria != null && !categoria.isEmpty()) {
      try{
        filtros.add(new FiltroCategoria(new Categoria(categoria)));
      } catch(Exception e){throw new RuntimeException("Categoria invalida");}
    }
    if(fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
      try{
        filtros.add(new FiltroFechaAcontecimiento(new RangoFechas(LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin))));
      } catch(Exception e){throw new RuntimeException("Rango fechas invalida");}
    }
    if(titulo != null && !titulo.isEmpty()) {
      try{
        filtros.add(new FiltroTitulo(titulo));
      } catch(Exception e){throw new RuntimeException("Titulo invalida");}
    }
    return filtros;
  }
}
