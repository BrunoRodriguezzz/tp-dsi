import models.entities.fuentes.Fuente;
import models.entities.utils.Errores.ER_ValueObjects.UbicacionInvalidaException;
import models.entities.valueObjectsHecho.*;
import models.entities.criterio.*;
import models.entities.hechos.Coleccion;
import models.entities.hechos.Hecho;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestColecciones {
    // ------------------------------------------------ Instanciacion de Hechos ------------------------------------------------
    Ubicacion ubicacion1;
    Categoria categoria1;
    Origen origen1 = Origen.MANUAL;
    Hecho hecho1 = new Hecho("Caída de aeronave impacta en Olavarría",
            "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
            categoria1,
            ubicacion1,
            LocalDate.of(2001, 11, 21),
            origen1
    );

    Ubicacion ubicacion2;
    Categoria categoria2;
    Origen origen2 = Origen.MANUAL;
    Hecho hecho2 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
            "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
            categoria2,
            ubicacion2,
            LocalDate.of(2001, 8, 16),
            origen2
    );

    Ubicacion ubicacion3;
    Categoria categoria3;
    Origen origen3 = Origen.MANUAL;
    Hecho hecho3 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
            "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
            categoria3,
            ubicacion3,
            LocalDate.of(2008, 8, 8),
            origen3
    );

    Ubicacion ubicacion4;
    Categoria categoria4;
    Origen origen4 = Origen.MANUAL;
    Hecho hecho4 = new Hecho("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires",
            "Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
            categoria4,
            ubicacion4,
            LocalDate.of(2020, 1, 27),
            origen4
    );

    Ubicacion ubicacion5;
    Categoria categoria5;
    Origen origen5 = Origen.MANUAL;
    Hecho hecho5 = new Hecho("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
            "Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
            categoria5,
            ubicacion5,
            LocalDate.of(2016, 6, 4),
            origen5
    );


    // ------------------------------------------------ Instanciacion de Coleccion ------------------------------------------------
    List<Hecho> hechos = Arrays.asList(hecho1, hecho2, hecho3, hecho4, hecho5);
    Coleccion coleccion;
    Criterio criterioPruebas;

    // ------------------------------------------------ Instanciacion de Fuente ------------------------------------------------
    Fuente fuenteMockeada = mock(Fuente.class);

    // ------------------------------------------------ Instanciacion de Rango de Fechas ------------------------------------------------
    RangoFechas rangoFechasFiltro;

    @BeforeEach
    public void setUp(){
        try {
            ubicacion1 = new Ubicacion("-36.868375", "-60.343297");
            ubicacion2 = new Ubicacion("-37.345571", "-70.241485");
            ubicacion3 = new Ubicacion("-33.768051", "-61.921032");
            ubicacion4 = new Ubicacion("-35.855811", "-61.940589");
            ubicacion5 = new Ubicacion("-26.780008", "-60.458782");
        } catch (UbicacionInvalidaException e) {
            fail("No debería fallar para coordenadas válidas");
        }
        try {
            categoria1= new Categoria("Caída de aeronave");
            categoria2= new Categoria("Accidente con maquinaria industrial");
            categoria3= new Categoria("Caída de aeronave");
            categoria4= new Categoria("Accidente en paso a nivel");
            categoria5= new Categoria("Derrumbe en obra en construcción");
        } catch (Exception e) {
            fail("No debería fallar por categoría inválida: " + e.getMessage() + "");
        }

        try {
            rangoFechasFiltro = new RangoFechas(LocalDate.parse("2000-01-01"), LocalDate.parse("2010-01-01"));
        }
        catch (Exception e){
            fail("No pudo instanciarse el rango de fechas: " + e.getMessage());
        }


        coleccion = new Coleccion("Colección prueba", "Esto es una prueba");
        criterioPruebas = new Criterio();

        coleccion.setFuente(fuenteMockeada);
        when(fuenteMockeada.importarHechos()).thenReturn(hechos);
    }

    @Test
    @DisplayName("Validar que se puedan obtener los hechos a partir de la colección.\n")
    public void obtenerHechos(){
        Assertions.assertEquals(hecho1, coleccion.consultarHechos().get(0));
    }

    //Criterios de pertenencia
    @Test
    @DisplayName("Al agregar un criterio de pertenencia y recalcular la coleccion deberían quedar solo los primeros tres")
    public void pruebaCriterios1(){
        FiltroFechaAcontecimiento filtroFechas = new FiltroFechaAcontecimiento(rangoFechasFiltro);

        criterioPruebas.agregarFiltro(filtroFechas);
        coleccion.setCriterio(criterioPruebas);

        Assertions.assertEquals(3,coleccion.consultarHechos().size());
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho1));
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho2));
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho3));

//      Al agregar un nuevo criterio de pertenencia y recalcular la coleccion el segundo ya no debería estar presente
        FiltroCategoria filtroCategoria = new FiltroCategoria(categoria1);

        criterioPruebas.agregarFiltro(filtroCategoria);

        Assertions.assertEquals(2,coleccion.consultarHechos().size());
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho1));
        Assertions.assertFalse(coleccion.consultarHechos().contains(hecho2));
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho3));
    }

    @Test
    @DisplayName("Sobre la colección aplicar un filtro de tipo categoría = “Caída de Aeronave” y título = ”un título”. Ningún hecho de la colección cumple con este filtro.")
    public void pruebaFiltros(){
        FiltroTitulo filtroTitulo = new FiltroTitulo("un titulo");
        FiltroCategoria filtroCategoria = new FiltroCategoria(categoria1);
        ArrayList<Filtro> filtros = new ArrayList<>();

        filtros.add(filtroTitulo);
        filtros.add(filtroCategoria);

        Assertions.assertTrue(coleccion.consultarHechos(filtros).isEmpty());
    }

    @Test
    @DisplayName("Etiquetar al hecho titulado “Caída de aeronave impacta en Olavarría” como “Olavarría” .\n" + "Etiquetar al mismo hecho como “Grave”.\n" + "Verificar que el hecho retenga las 2 etiquetas correspondientes.\n")
    public void pruebaEtiquetas(){
        Etiqueta etiquetaOlavarria = new Etiqueta("Olavarría");
        Etiqueta etiquetaGrave = new Etiqueta("Grave");

        hecho1.agregarEtiqueta(etiquetaOlavarria);
        hecho1.agregarEtiqueta(etiquetaGrave);

        Assertions.assertTrue(hecho1.getEtiquetas().contains(etiquetaOlavarria));
        Assertions.assertTrue(hecho1.getEtiquetas().contains(etiquetaGrave));
    }
}
