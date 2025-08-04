package ar.edu.utn.frba.dds.agregador.models.domain.test;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.IStratConsenso;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl.AlgConsensoAbsoluto;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl.AlgConsensoMaySimple;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.impl.AlgConsensoMultMenciones;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Ubicacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsensoTest {
      Hecho hecho1 = null;
      Hecho hecho2 = null;
      Hecho hecho3 = null;
      Hecho hecho4 = null;
      Hecho hecho5 = null;

      Origen origen1 = Origen.MANUAL;

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

      Fuente fuenteMockeada1 = Mockito.mock(Fuente.class);
      Fuente fuenteMockeada2 = Mockito.mock(Fuente.class);
      Fuente fuenteMockeada3 = Mockito.mock(Fuente.class);
      Fuente fuenteMockeada4 = Mockito.mock(Fuente.class);
      Fuente fuenteMockeada5 = Mockito.mock(Fuente.class);

      List<Hecho> hechos = new ArrayList<>();
      List<Fuente> fuentes = new ArrayList<>();

      @BeforeEach
      public void setUp(){
        Mockito.when(fuenteMockeada1.getId()).thenReturn(1L);
        Mockito.when(fuenteMockeada2.getId()).thenReturn(2L);
        Mockito.when(fuenteMockeada3.getId()).thenReturn(3L);
        Mockito.when(fuenteMockeada4.getId()).thenReturn(4L);
        Mockito.when(fuenteMockeada5.getId()).thenReturn(5L);

        try {
          ubicacion1 = new Ubicacion("-36.868375", "-60.343297");
          ubicacion2 = ubicacion1;
          ubicacion3 = ubicacion1;
          ubicacion4 = ubicacion1;
          ubicacion5 = ubicacion1;

          categoria1 = new Categoria("Caída de aeronave");
          categoria2 = categoria1;
          categoria3 = categoria1;
          categoria4 = categoria1;
          categoria5 = categoria1;

          hecho1 = new Hecho("Caída de aeronave impacta en Olavarría",
              "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
              categoria1,
              ubicacion1,
              LocalDate.of(2001, 11, 21),
              origen1
          );
          hecho1.setEstaEliminado(false);
          hecho1.setFuente(fuenteMockeada1);

          hecho2 = new Hecho("Caída de aeronave impacta en Olavarría",
              "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
              categoria1,
              ubicacion1,
              LocalDate.of(2001, 11, 21),
              origen1
          );
          hecho2.setEstaEliminado(false);
          hecho2.setFuente(fuenteMockeada2);

          hecho3 = new Hecho("Caída de aeronave impacta en Olavarría",
              "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
              categoria1,
              ubicacion1,
              LocalDate.of(2001, 11, 21),
              origen1
          );
          hecho3.setEstaEliminado(false);
          hecho3.setFuente(fuenteMockeada3);

          hecho4 = new Hecho("Caída de aeronave impacta en Olavarría",
              "Descripción cambiada 4",
              categoria1,
              ubicacion1,
              LocalDate.of(2001, 11, 21),
              origen1
          );
          hecho4.setEstaEliminado(false);
          hecho4.setFuente(fuenteMockeada4);

          hecho5 = new Hecho("Caída de aeronave impacta en Olavarría",
              "Descripción cambiada 5",
              categoria1,
              ubicacion1,
              LocalDate.of(2001, 11, 21),
              origen1
          );
          hecho5.setEstaEliminado(false);
          hecho5.setFuente(fuenteMockeada5);

        } catch (Exception e) {
          fail("No se pudo inicializar el test");
        }
      }
    @Test
    @DisplayName("Si todas las fuentes tienen exactamente el mismo Hecho todos los hechos tendrán todos los consensos\n")
    public void casoPositivoDeTodosLosAlgoritmos(){
      List<Hecho> hechos = new ArrayList<>();
      hechos.add(hecho1);
      hechos.add(hecho2);
      hechos.add(hecho3);
      fuentes.add(fuenteMockeada1);
      fuentes.add(fuenteMockeada2);
      fuentes.add(fuenteMockeada3);

      List<Hecho> hechosConsensuados = new ArrayList<>();

      List<IStratConsenso> strats = new ArrayList<>();
      strats.add(AlgConsensoMultMenciones.getInstance());
      strats.add(AlgConsensoMaySimple.getInstance());
      strats.add(AlgConsensoAbsoluto.getInstance());

      strats.forEach(strat -> {
        strat.consensuados(hechos, fuentes, hecho1);
      });
      hechosConsensuados.add(hecho1);

      Coleccion unaColeccion = new Coleccion("Fuente Ejemplo", "Esta es una fuente de ejemplo", fuentes, new Criterio());
      List<Consenso> consensos = new ArrayList<>();
      consensos.add(Consenso.ABSOLUTO);
      consensos.add(Consenso.MAYSIMPLE);
      consensos.add(Consenso.MULTMENCIONES);

      unaColeccion.setConsensos(consensos);
      unaColeccion.cargarHecho(hecho1);
      unaColeccion.cargarHecho(hecho4);
      unaColeccion.cargarHecho(hecho5);

      assertEquals(3, hecho1.getConsensos().size());
      assertTrue(hecho1.getConsensos().contains(Consenso.MULTMENCIONES));
      assertTrue(hecho1.getConsensos().contains(Consenso.MAYSIMPLE));
      assertTrue(hecho1.getConsensos().contains(Consenso.ABSOLUTO));
      assertEquals(1, unaColeccion.consultarHechosCurados().size());
    }

    @Test
    @DisplayName("Si al menos dos fuentes tienen el mismo hecho y otra fuente contiene otro de igual titulo pero diferntes atributos no es consensuado por mult menciones\n")
    public void casoNegativoMultMenciones(){
      List<Hecho> hechos = new ArrayList<>();
      hechos.add(hecho1);
      hechos.add(hecho2);
      hechos.add(hecho4);
      fuentes.add(fuenteMockeada1);
      fuentes.add(fuenteMockeada2);
      fuentes.add(fuenteMockeada4);

      List<Hecho> hechosConsensuados = new ArrayList<>();

      List<IStratConsenso> strats = new ArrayList<>();
      strats.add(AlgConsensoMultMenciones.getInstance());

      strats.forEach(strat -> {
        Hecho aux = strat.consensuados(hechos, fuentes, hecho1);
        hechosConsensuados.add(aux);
      });

      assertFalse(hecho1.getConsensos().contains(Consenso.MULTMENCIONES));
    }

    @Test
    @DisplayName("Si al menos la mitad de las fuentes contienen el mismo hecho el hecho cumple con mayoria simple\n")
    public void casoNegativoMaySimple(){
      List<Hecho> hechos = new ArrayList<>();
      hechos.add(hecho1);
      hechos.add(hecho4);
      hechos.add(hecho5);
      fuentes.add(fuenteMockeada1);
      fuentes.add(fuenteMockeada4);
      fuentes.add(fuenteMockeada5);

      List<Hecho> hechosConsensuados = new ArrayList<>();

      List<IStratConsenso> strats = new ArrayList<>();
      strats.add(AlgConsensoMaySimple.getInstance());

      strats.forEach(strat -> {
        Hecho aux = strat.consensuados(hechos, fuentes, hecho1);
        hechosConsensuados.add(aux);
      });

      assertFalse(hecho1.getConsensos().contains(Consenso.MAYSIMPLE));
    }
}
