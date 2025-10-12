package ar.edu.utn.frba.dds.agregador.models.domain.test;

import ar.edu.utn.frba.dds.agregador.models.domain.comparador.ComparadorHechos;
import ar.edu.utn.frba.dds.agregador.models.domain.comparador.impl.ComparadorUbicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComparadorHechosTest {

    private Categoria categoriaIncendio;
    private Categoria categoriaInundacion;
    private Ubicacion ubicacionNY;
    private Ubicacion ubicacionNYCerca;
    private Ubicacion ubicacionLA;
    private LocalDateTime fecha15Enero;
    private LocalDateTime fecha16Enero;

    @BeforeEach
    void setUp() {
        // Inicializar todos los objetos de prueba
        categoriaIncendio = new Categoria();
        categoriaIncendio.setTitulo("Incendio");

        categoriaInundacion = new Categoria();
        categoriaInundacion.setTitulo("Inundación");

        ubicacionNY = new Ubicacion();
        ubicacionNY.setLatitud(40.7128);
        ubicacionNY.setLongitud(-74.0060);

        ubicacionNYCerca = new Ubicacion();
        ubicacionNYCerca.setLatitud(40.7130);
        ubicacionNYCerca.setLongitud(-74.0062);

        ubicacionLA = new Ubicacion();
        ubicacionLA.setLatitud(34.0522);
        ubicacionLA.setLongitud(-118.2437);

        fecha15Enero = LocalDateTime.of(2024, 1, 15, 10, 0);
        fecha16Enero = LocalDateTime.of(2024, 1, 16, 10, 0);
    }

    private Hecho crearHecho(String titulo, String descripcion, Categoria categoria,
                             Ubicacion ubicacion, LocalDateTime fecha) {
        try {
            Origen origen = Origen.MANUAL;
            Hecho hecho = new Hecho(titulo, descripcion, categoria, ubicacion, fecha, origen);
            hecho.setCategoria(categoria);
            return hecho;
        } catch (Exception e) {
            throw new RuntimeException("Error creando hecho de prueba: " + e.getMessage(), e);
        }
    }

    @Test
    void testHechosIdenticos_DeberiaRetornarTrue() {
        ComparadorHechos comparador = new ComparadorHechos();

        Hecho hecho1 = crearHecho("Incendio edificio", "Descripción 1",
                categoriaIncendio, ubicacionNY, fecha15Enero);
        Hecho hecho2 = crearHecho("Incendio edificio", "Descripción 1",
                categoriaIncendio, ubicacionNY, fecha15Enero);

        assertTrue(comparador.comparar(hecho1, hecho2));
    }

    @Test
    void testHechosSimilaresMismaFechaUbicacionCercana_DeberiaRetornarTrue() {
        ComparadorHechos comparador = new ComparadorHechos();

        Hecho hecho1 = crearHecho("Incendio A", "Descripción A",
                categoriaIncendio, ubicacionNY, fecha15Enero);
        Hecho hecho2 = crearHecho("Incendio B", "Descripción B",
                categoriaIncendio, ubicacionNYCerca, fecha15Enero);

        assertTrue(comparador.comparar(hecho1, hecho2));
    }

    @Test
    void testHechosDiferenteCategoria_DeberiaRetornarFalse() {
        ComparadorHechos comparador = new ComparadorHechos();

        Hecho hecho1 = crearHecho("Incendio", "Descripción",
                categoriaIncendio, ubicacionNY, fecha15Enero);
        Hecho hecho2 = crearHecho("Inundación", "Descripción",
                categoriaInundacion, ubicacionNY, fecha15Enero);

        assertFalse(comparador.comparar(hecho1, hecho2));
    }

    @Test
    void testHechosMismaCategoriaUbicacionLejana_DeberiaRetornarFalse() {
        ComparadorHechos comparador = new ComparadorHechos();

        Hecho hecho1 = crearHecho("Incendio NY", "Descripción",
                categoriaIncendio, ubicacionNY, fecha15Enero);
        Hecho hecho2 = crearHecho("Incendio LA", "Descripción",
                categoriaIncendio, ubicacionLA, fecha15Enero);

        assertFalse(comparador.comparar(hecho1, hecho2));
    }

    @Test
    void testHechosMismaCategoriaDiferenteFecha_DeberiaRetornarFalse() {
        ComparadorHechos comparador = new ComparadorHechos();

        Hecho hecho1 = crearHecho("Incendio", "Descripción",
                categoriaIncendio, ubicacionNY, fecha15Enero);
        Hecho hecho2 = crearHecho("Incendio", "Descripción",
                categoriaIncendio, ubicacionNY, fecha16Enero);

        assertFalse(comparador.comparar(hecho1, hecho2));
    }

    @Test
    void testHechosConNull_DeberiaRetornarFalse() {
        ComparadorHechos comparador = new ComparadorHechos();

        Hecho hechoValido = crearHecho("Incendio", "Descripción",
                categoriaIncendio, ubicacionNY, fecha15Enero);

        assertFalse(comparador.comparar(hechoValido, null));
        assertFalse(comparador.comparar(null, hechoValido));
        assertFalse(comparador.comparar(null, null));
    }

    @Test
    void testHechosCategoriaCaseInsensitive_DeberiaRetornarTrue() {
        ComparadorHechos comparador = new ComparadorHechos();

        Categoria categoriaMayusculas = new Categoria();
        categoriaMayusculas.setTitulo("INCENDIO");

        Categoria categoriaMinusculas = new Categoria();
        categoriaMinusculas.setTitulo("incendio");

        Hecho hecho1 = crearHecho("Incendio", "Descripción",
                categoriaMayusculas, ubicacionNY, fecha15Enero);
        Hecho hecho2 = crearHecho("Incendio", "Descripción",
                categoriaMinusculas, ubicacionNY, fecha15Enero);

        assertTrue(comparador.comparar(hecho1, hecho2));
    }

    @Test
    void testHechosConMargenPersonalizado_DeberiaRetornarTrue() {
        ComparadorHechos comparador = new ComparadorHechos();
        // Configurar margen más amplio
        comparador.setComparadorUbicacion(ComparadorUbicacion.conRadioKilometros(10));

        // Ubicaciones con ~5km de distancia (dentro de 10km)
        Ubicacion ubicacion1 = new Ubicacion();
        ubicacion1.setLatitud(40.7128);
        ubicacion1.setLongitud(-74.0060);

        Ubicacion ubicacion2 = new Ubicacion();
        ubicacion2.setLatitud(40.7500);
        ubicacion2.setLongitud(-73.9900);

        Hecho hecho1 = crearHecho("Manifestación", "Descripción",
                categoriaIncendio, ubicacion1, fecha15Enero);
        Hecho hecho2 = crearHecho("Manifestación", "Descripción",
                categoriaIncendio, ubicacion2, fecha15Enero);

        assertTrue(comparador.comparar(hecho1, hecho2));
    }
}