package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.ISeederService;
import ar.edu.utn.frba.dds.agregador.services.impl.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.Criterio;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroCategoria;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroFechaAcontecimiento;
import ar.edu.utn.frba.dds.agregador.models.domain.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.RangoFechas;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeederService implements ISeederService {
  @Autowired
  private IColeccionRepository coleccionRepository;

  @Autowired
  private IHechoRepository hechoRepository;

  private IFuenteService fuenteService;

  public SeederService(IFuenteService fuenteService) {
    this.fuenteService = fuenteService;
  }

  public void init(){
    List<Coleccion> colecciones = new ArrayList<Coleccion>();
    List<Hecho> hechos = new ArrayList<>();
    Hecho hecho1 = null;
    Hecho hecho2 = null;
    Hecho hecho3 = null;
    Hecho hecho4 = null;
    Hecho hecho5 = null;
    Origen origen1 = Origen.MANUAL;
    Origen origen2 = Origen.MANUAL;
    Origen origen3 = Origen.MANUAL;
    Origen origen4 = Origen.MANUAL;
    Origen origen5 = Origen.MANUAL;
    Ubicacion ubicacion1 = null;
    Ubicacion ubicacion2 = null;
    Ubicacion ubicacion3 = null;
    Ubicacion ubicacion4 = null;
    Ubicacion ubicacion5 = null;
    Categoria categoria1 = null;
    Categoria categoria2 = null;
    Categoria categoria3 = null;
    Categoria categoria4 = null;
    Categoria categoria5 = null;
    try {
      ubicacion1 = new Ubicacion("-36.868375", "-60.343297");
      ubicacion2 = new Ubicacion("-37.345571", "-70.241485");
      ubicacion3 = new Ubicacion("-33.768051", "-61.921032");
      ubicacion4 = new Ubicacion("-35.855811", "-61.940589");
      ubicacion5 = new Ubicacion("-26.780008", "-60.458782");

      categoria1 = new Categoria("Caída de aeronave");
      categoria2 = new Categoria("Accidente con maquinaria industrial");
      categoria3 = new Categoria("Caída de aeronave");
      categoria4 = new Categoria("Accidente en paso a nivel");
      categoria5 = new Categoria("Derrumbe en obra en construcción");
      hecho1 = new Hecho("Caída de aeronave impacta en Olavarría",
          "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
          categoria1,
          ubicacion1,
          LocalDate.of(2001, 11, 21),
          origen1
      );
      hecho1.setEstaEliminado(false);
      hecho1.setFuente("Accidentes");
      hecho1.setId(1123L);

      hecho2 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
          "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
          categoria2,
          ubicacion2,
          LocalDate.of(2001, 8, 16),
          origen2
      );
      hecho2.setEstaEliminado(false);
      hecho2.setFuente("Incidentes");
      hecho2.setId(1124L);

      hecho3 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
          "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
          categoria3,
          ubicacion3,
          LocalDate.of(2008, 8, 8),
          origen3
      );
      hecho3.setEstaEliminado(false);
      hecho3.setFuente("Incidentes");
      hecho3.setId(1125L);

      hecho4 = new Hecho("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires",
          "Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
          categoria4,
          ubicacion4,
          LocalDate.of(2020, 1, 27),
          origen4
      );
      hecho4.setEstaEliminado(false);
      hecho4.setId(1126L);
      hecho4.setFuente("Accidentes");

      hecho5 = new Hecho("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
          "Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
          categoria5,
          ubicacion5,
          LocalDate.of(2016, 6, 4),
          origen5
      );
      hecho5.setEstaEliminado(false);
      hecho5.setId(1127L);
      hecho5.setFuente("Derrumbes");


      hechos.add(hecho1);
      hechos.add(hecho2);
      hechos.add(hecho3);
      hechos.add(hecho4);
      hechos.add(hecho5);
    } catch (Exception e) {
      System.out.println("No se pudieron inicializar los hechos");
    }

    Coleccion coleccion;
    coleccion = new Coleccion("Colección prueba", "Esto es una prueba");
    List<String> fuentes = new ArrayList<>();
    fuentes.add("Accidentes");
    fuentes.add("Incidentes");
    fuentes.add("Derrumbes");

    coleccion.setFuentes(fuentes);

    coleccion.setId(123456L);

    Criterio criterioPruebas;
    criterioPruebas = new Criterio();

//    coleccion.cargarHechos(hechos);
    coleccion.setCriterio(criterioPruebas);

    RangoFechas rangoFechasFiltro = null;
    try {
      rangoFechasFiltro = new RangoFechas(LocalDate.parse("2000-01-01"), LocalDate.parse("2010-01-01"));
    } catch (
        FechaInvalidaException e) {
      throw new RuntimeException(e);
    }
    FiltroFechaAcontecimiento filtroFechas = new FiltroFechaAcontecimiento(rangoFechasFiltro);

    coleccion.agregarFiltroACriterio(filtroFechas);
//    coleccion.recalcularHechos();

    Coleccion coleccion1;
    coleccion1 = new Coleccion("Colección prueba 1", "Esto es una prueba");

    coleccion1.setFuentes(fuentes);

    coleccion1.setId(123457L);

    Criterio criterioPruebas1;
    criterioPruebas1 = new Criterio();

//    coleccion1.cargarHechos(hechos);
    coleccion1.setCriterio(criterioPruebas1);

    FiltroCategoria filtroCategoria = null;
    try {
      filtroCategoria = new FiltroCategoria(new Categoria("Caída de aeronave"));
    } catch (Exception e) {
      System.out.println("No se pudieron inicializar los categorias");
    }

    coleccion1.agregarFiltroACriterio(filtroCategoria);
//    coleccion1.recalcularHechos();

    this.coleccionRepository.guardarColeccion(coleccion);
    this.coleccionRepository.guardarColeccion(coleccion1);
    this.hechoRepository.inicializarHechos(hechos);
    this.fuenteService.agregarFuenteAdapter(new FuenteAdapter("http://localhost:8060", TipoFuente.ESTATICA));
    this.fuenteService.agregarFuenteAdapter(new FuenteAdapter("http://localhost:8061", TipoFuente.DINAMICA));
    this.fuenteService.agregarFuenteAdapter(new FuenteAdapter("http://localhost:8062", TipoFuente.PROXY));
  }
}
