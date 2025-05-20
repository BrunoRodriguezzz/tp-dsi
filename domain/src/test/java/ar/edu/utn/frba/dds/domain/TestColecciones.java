package ar.edu.utn.frba.dds.domain;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.Criterio;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.Filtro;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroCategoria;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroFechaAcontecimiento;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.FiltroTitulo;
import ar.edu.utn.frba.dds.domain.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Etiqueta;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.RangoFechas;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
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
    public TestColecciones() {
    }
        Hecho hecho1 = null;
        Hecho hecho2 = null;
        Hecho hecho3 = null;
        Hecho hecho4 = null;
        Hecho hecho5 = null;

        RangoFechas rangoFechasFiltro = null;

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

        Coleccion coleccion;

        Fuente fuenteMockeada = mock(Fuente.class);

        Criterio criterioPruebas;

    @BeforeEach
    public void setUp(){

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

            rangoFechasFiltro = new RangoFechas(LocalDate.parse("2000-01-01"), LocalDate.parse("2010-01-01"));

            hecho1 = new Hecho("Caída de aeronave impacta en Olavarría",
                    "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                    categoria1,
                    ubicacion1,
                    LocalDate.of(2001, 11, 21),
                    origen1
            );

            hecho2 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
                    "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                    categoria2,
                    ubicacion2,
                    LocalDate.of(2001, 8, 16),
                    origen2
            );

            hecho3 = new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
                    "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                    categoria3,
                    ubicacion3,
                    LocalDate.of(2008, 8, 8),
                    origen3
            );

            hecho4 = new Hecho("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires",
                    "Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
                    categoria4,
                    ubicacion4,
                    LocalDate.of(2020, 1, 27),
                    origen4
            );

            hecho5 = new Hecho("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
                    "Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
                    categoria5,
                    ubicacion5,
                    LocalDate.of(2016, 6, 4),
                    origen5
            );
        } catch (Exception e) {
            fail("No se pudo inicializar el test");
        }

        // ------------------------------------------------ Instanciacion de Coleccion ------------------------------------------------
        List<Hecho> hechos = Arrays.asList(hecho1, hecho2, hecho3, hecho4, hecho5);

        // ------------------------------------------------ Instanciacion de Fuente ------------------------------------------------



        coleccion = new Coleccion("Colección prueba", "Esto es una prueba");
        criterioPruebas = new Criterio();

        coleccion.setFuente(fuenteMockeada);
        when(fuenteMockeada.importarHechos()).thenReturn(hechos);
    }

    @Test
    @DisplayName("Validar que se puedan obtener los hechos a partir de la colección.\n")
    public void obtenerHechos(){
        List<Hecho> hechos = fuenteMockeada.importarHechos();
        coleccion.cargarHechos(hechos);
        Assertions.assertEquals(hecho1, coleccion.consultarHechos().get(0));
    }

    //Criterios de pertenencia
    @Test
    @DisplayName("Al agregar un criterio de pertenencia y recalcular la coleccion deberían quedar solo los primeros tres")
    public void pruebaCriterios1(){
        FiltroFechaAcontecimiento filtroFechas = new FiltroFechaAcontecimiento(rangoFechasFiltro);

        List<Hecho> hechos = fuenteMockeada.importarHechos();
        coleccion.cargarHechos(hechos);
        coleccion.setCriterio(criterioPruebas);
        coleccion.agregarFiltroACriterio(filtroFechas);
        coleccion.recalcularHechos();

        Assertions.assertEquals(3,coleccion.consultarHechos().size());
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho1));
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho2));
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho3));

//      Al agregar un nuevo criterio de pertenencia y recalcular la coleccion el segundo ya no debería estar presente
        FiltroCategoria filtroCategoria = new FiltroCategoria(categoria1);

        coleccion.agregarFiltroACriterio(filtroCategoria);
        coleccion.recalcularHechos();

        Assertions.assertEquals(2,coleccion.consultarHechos().size());
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho1));
        Assertions.assertFalse(coleccion.consultarHechos().contains(hecho2));
        Assertions.assertTrue(coleccion.consultarHechos().contains(hecho3));
    }

    @Test
    @DisplayName("Sobre la colección aplicar un filtro de tipo categoría = “Caída de Aeronave” y título = ”un título”. Ningún hecho de la colección cumple con este filtro.")
    public void pruebaFiltros(){

        List<Hecho> hechos = fuenteMockeada.importarHechos();
        coleccion.cargarHechos(hechos);

        FiltroTitulo filtroTitulo = new FiltroTitulo("un titulo");
        FiltroCategoria filtroCategoria = new FiltroCategoria(categoria1);
        ArrayList<Filtro> filtros = new ArrayList<>();

        filtros.add(filtroTitulo);
        filtros.add(filtroCategoria);

        Assertions.assertTrue(coleccion.consultarHechos(filtros).isEmpty());
    }

    @Test
    @DisplayName("""
        Etiquetar al hecho titulado “Caída de aeronave impacta en Olavarría” como “Olavarría” .
        Etiquetar al mismo hecho como “Grave”.
        Verificar que el hecho retenga las 2 etiquetas correspondientes.
        """)
    public void pruebaEtiquetas(){
        Etiqueta etiquetaOlavarria = new Etiqueta("Olavarría");
        Etiqueta etiquetaGrave = new Etiqueta("Grave");

        hecho1.agregarEtiqueta(etiquetaOlavarria);
        hecho1.agregarEtiqueta(etiquetaGrave);

        Assertions.assertTrue(hecho1.getEtiquetas().contains(etiquetaOlavarria));
        Assertions.assertTrue(hecho1.getEtiquetas().contains(etiquetaGrave));
    }
}
