package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FuenteService implements IFuenteService {
  List<Hecho> hechos;

  // TODO: En principio lo hago sincrónico porque no tengo las APIs, cuando las tenga hago el fix con Mono
  // Constructor
  public FuenteService() {
    this.hechos = new ArrayList<>();
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

      hecho2 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
          "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
          categoria2,
          ubicacion2,
          LocalDate.of(2001, 8, 16),
          origen2
      );
      hecho2.setEstaEliminado(false);

      hecho3 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
          "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
          categoria3,
          ubicacion3,
          LocalDate.of(2008, 8, 8),
          origen3
      );
      hecho3.setEstaEliminado(false);

      hecho4 = new Hecho("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires",
          "Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
          categoria4,
          ubicacion4,
          LocalDate.of(2020, 1, 27),
          origen4
      );
      hecho4.setEstaEliminado(false);

      hecho5 = new Hecho("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
          "Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
          categoria5,
          ubicacion5,
          LocalDate.of(2016, 6, 4),
          origen5
      );
      hecho5.setEstaEliminado(false);

      this.hechos.add(hecho1);
      this.hechos.add(hecho2);
      this.hechos.add(hecho3);
      this.hechos.add(hecho4);
      this.hechos.add(hecho5);
    } catch (Exception e) {
      System.out.println("No se pudieron inicializar los hechos");
    }
  }
  public List<Hecho> buscarHechos(){
    return this.hechos;
  }
}

