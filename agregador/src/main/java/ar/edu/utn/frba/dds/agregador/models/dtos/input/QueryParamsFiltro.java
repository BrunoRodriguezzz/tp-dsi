package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroCategoria;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaAcontecimientoFinal;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaAcontecimientoInicio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaCargaFinal;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaCargaInicio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroLatitud;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroLongitud;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroTitulo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@Setter
public class QueryParamsFiltro {
  public String categoria;
  public LocalDateTime fechaAcontecimientoInicio;
  public LocalDateTime fechaAcontecimientoFin;
  public LocalDateTime fechaCargaInicio;
  public LocalDateTime fechaCargaFin;
  public String longitud;
  public String latitud;
  public String titulo;

  public List<Filtro> instanciarFiltros() {
    List<Filtro> filtros = new ArrayList<>();
    if(categoria != null && !categoria.isEmpty()) {
      try{
        filtros.add(new FiltroCategoria(new Categoria(categoria)));
      } catch(Exception e){throw new RuntimeException("Categoria invalida");}
    }
    if(latitud != null && !latitud.isEmpty()) {
      try{
        filtros.add(new FiltroLatitud(Double.parseDouble(latitud)));
      } catch(Exception e){throw new RuntimeException("Latitud invalida " + e.getMessage());}
    }
    if(longitud != null && !longitud.isEmpty()) {
      try{
        filtros.add(new FiltroLongitud(Double.parseDouble(longitud)));
      } catch(Exception e){throw new RuntimeException("Longitud invalida " + e.getMessage());}
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
    if(fechaCargaInicio != null) {
      FiltroFechaCargaInicio filtroFechaCargaInicio = new FiltroFechaCargaInicio();
      filtroFechaCargaInicio.setFechaInicio(this.fechaCargaInicio);
      filtros.add(filtroFechaCargaInicio);
    }
    if (fechaCargaFin != null) {
      FiltroFechaCargaFinal filtroFechaCargaFin = new FiltroFechaCargaFinal();
      filtroFechaCargaFin.setFechaFinal(this.fechaCargaFin);
      filtros.add(filtroFechaCargaFin);
    }
    if(titulo != null && !titulo.isEmpty()) {
      try{
        filtros.add(new FiltroTitulo(titulo));
      } catch(Exception e){throw new RuntimeException("Titulo invalida");}
    }
    return filtros;
  }
}
