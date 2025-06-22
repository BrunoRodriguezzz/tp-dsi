package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.domain.models.entities.criterio.Filtro;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroCategoria;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroFechaAcontecimiento;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroTitulo;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.RangoFechas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
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
