package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.UbicacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Provincia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GeneradorDatosPrueba {

    /**
     * Genera una lista de colecciones con hechos para testing
     */
    public static List<ColeccionInputDTO> generarColecciones() {
        List<ColeccionInputDTO> colecciones = new ArrayList<>();

        // === COLECCION 1: "Hechos Historicos Argentinos" ===
        ColeccionInputDTO coleccion1 = new ColeccionInputDTO();
        coleccion1.setId(1L);
        coleccion1.setTitulo("Hechos Historicos Argentinos");
        coleccion1.setDescripcion("Eventos historicos importantes de Argentina");

        List<HechoInputDTO> hechos1 = new ArrayList<>();

        // Hecho 1.1: BUENOS_AIRES - Historico - H10
        HechoInputDTO hecho1_1 = new HechoInputDTO();
        hecho1_1.setId(1L);
        hecho1_1.setTitulo("Revolucion de Mayo");
        hecho1_1.setCategoria("Historico");
        hecho1_1.setFechaAcontecimiento(LocalDateTime.of(1810, 5, 25, 10, 0));
        UbicacionInputDTO ubicacion1_1 = new UbicacionInputDTO();
        ubicacion1_1.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
        hecho1_1.setUbicacion(ubicacion1_1);
        hechos1.add(hecho1_1);

        // Hecho 1.2: BUENOS_AIRES - Historico - H10 (misma clave que 1.1)
        HechoInputDTO hecho1_2 = new HechoInputDTO();
        hecho1_2.setId(2L);
        hecho1_2.setTitulo("Independencia Argentina");
        hecho1_2.setCategoria("Historico");
        hecho1_2.setFechaAcontecimiento(LocalDateTime.of(1816, 7, 9, 10, 30));
        UbicacionInputDTO ubicacion1_2 = new UbicacionInputDTO();
        ubicacion1_2.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
        hecho1_2.setUbicacion(ubicacion1_2);
        hechos1.add(hecho1_2);

        // Hecho 1.3: CORDOBA - Politico - H14
        HechoInputDTO hecho1_3 = new HechoInputDTO();
        hecho1_3.setId(3L);
        hecho1_3.setTitulo("Cordobazo");
        hecho1_3.setCategoria("Politico");
        hecho1_3.setFechaAcontecimiento(LocalDateTime.of(1969, 5, 29, 14, 0));
        UbicacionInputDTO ubicacion1_3 = new UbicacionInputDTO();
        ubicacion1_3.setProvincia(Provincia.valueOf("CORDOBA"));
        hecho1_3.setUbicacion(ubicacion1_3);
        hechos1.add(hecho1_3);

        // Hecho 1.4: SANTA_FE - Cultural - H09
        HechoInputDTO hecho1_4 = new HechoInputDTO();
        hecho1_4.setId(4L);
        hecho1_4.setTitulo("Fundacion de Santa Fe");
        hecho1_4.setCategoria("Cultural");
        hecho1_4.setFechaAcontecimiento(LocalDateTime.of(1573, 11, 15, 9, 0));
        UbicacionInputDTO ubicacion1_4 = new UbicacionInputDTO();
        ubicacion1_4.setProvincia(Provincia.valueOf("SANTA_FE"));
        hecho1_4.setUbicacion(ubicacion1_4);
        hechos1.add(hecho1_4);

        coleccion1.setHechos(hechos1);
        colecciones.add(coleccion1);

        // === COLECCION 2: "Eventos Culturales" ===
        ColeccionInputDTO coleccion2 = new ColeccionInputDTO();
        coleccion2.setId(2L);
        coleccion2.setTitulo("Eventos Culturales");
        coleccion2.setDescripcion("Importantes eventos culturales del pais");

        List<HechoInputDTO> hechos2 = new ArrayList<>();

        // Hecho 2.1: BUENOS_AIRES - Cultural - H20
        HechoInputDTO hecho2_1 = new HechoInputDTO();
        hecho2_1.setId(5L);
        hecho2_1.setTitulo("Inauguracion Teatro Colon");
        hecho2_1.setCategoria("Cultural");
        hecho2_1.setFechaAcontecimiento(LocalDateTime.of(1908, 5, 25, 20, 0));
        UbicacionInputDTO ubicacion2_1 = new UbicacionInputDTO();
        ubicacion2_1.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
        hecho2_1.setUbicacion(ubicacion2_1);
        hechos2.add(hecho2_1);

        // Hecho 2.2: CORDOBA - Cultural - H18
        HechoInputDTO hecho2_2 = new HechoInputDTO();
        hecho2_2.setId(6L);
        hecho2_2.setTitulo("Festival de Cosquin");
        hecho2_2.setCategoria("Cultural");
        hecho2_2.setFechaAcontecimiento(LocalDateTime.of(1961, 1, 21, 18, 0));
        UbicacionInputDTO ubicacion2_2 = new UbicacionInputDTO();
        ubicacion2_2.setProvincia(Provincia.valueOf("CORDOBA"));
        hecho2_2.setUbicacion(ubicacion2_2);
        hechos2.add(hecho2_2);

        // Hecho 2.3: BUENOS_AIRES - Historico - H12
        HechoInputDTO hecho2_3 = new HechoInputDTO();
        hecho2_3.setId(7L);
        hecho2_3.setTitulo("Fundacion de Buenos Aires");
        hecho2_3.setCategoria("Historico");
        hecho2_3.setFechaAcontecimiento(LocalDateTime.of(1536, 2, 3, 12, 0));
        UbicacionInputDTO ubicacion2_3 = new UbicacionInputDTO();
        ubicacion2_3.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
        hecho2_3.setUbicacion(ubicacion2_3);
        hechos2.add(hecho2_3);

        coleccion2.setHechos(hechos2);
        colecciones.add(coleccion2);

        return colecciones;
    }

    /**
     * Genera una lista de hechos independientes (sin coleccion) para testing
     */
    public static List<HechoInputDTO> generarHechosIndependientes() {
        List<HechoInputDTO> hechos = new ArrayList<>();

        // Hecho independiente 1: MENDOZA - Politico - H06
        HechoInputDTO hecho1 = new HechoInputDTO();
        hecho1.setId(101L);
        hecho1.setTitulo("Cruce de los Andes");
        hecho1.setCategoria("Politico");
        hecho1.setFechaAcontecimiento(LocalDateTime.of(1817, 1, 18, 6, 0));
        UbicacionInputDTO ubicacion1 = new UbicacionInputDTO();
        ubicacion1.setProvincia(Provincia.valueOf("MENDOZA"));
        hecho1.setUbicacion(ubicacion1);
        hechos.add(hecho1);

        // Hecho independiente 2: BUENOS_AIRES - Cultural - H22
        HechoInputDTO hecho2 = new HechoInputDTO();
        hecho2.setId(102L);
        hecho2.setTitulo("Primer Tango");
        hecho2.setCategoria("Cultural");
        hecho2.setFechaAcontecimiento(LocalDateTime.of(1880, 12, 1, 22, 0));
        UbicacionInputDTO ubicacion2 = new UbicacionInputDTO();
        ubicacion2.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
        hecho2.setUbicacion(ubicacion2);
        hechos.add(hecho2);

        // Hecho independiente 3: SANTA_FE - Historico - H15
        HechoInputDTO hecho3 = new HechoInputDTO();
        hecho3.setId(103L);
        hecho3.setTitulo("Constitucion Nacional");
        hecho3.setCategoria("Historico");
        hecho3.setFechaAcontecimiento(LocalDateTime.of(1853, 5, 1, 15, 0));
        UbicacionInputDTO ubicacion3 = new UbicacionInputDTO();
        ubicacion3.setProvincia(Provincia.valueOf("SANTA_FE"));
        hecho3.setUbicacion(ubicacion3);
        hechos.add(hecho3);

        // Hecho independiente 4: MENDOZA - Politico - H06 (misma clave que hecho1)
        HechoInputDTO hecho4 = new HechoInputDTO();
        hecho4.setId(104L);
        hecho4.setTitulo("Campaña Libertadora");
        hecho4.setCategoria("Politico");
        hecho4.setFechaAcontecimiento(LocalDateTime.of(1817, 2, 12, 6, 30));
        UbicacionInputDTO ubicacion4 = new UbicacionInputDTO();
        ubicacion4.setProvincia(Provincia.valueOf("MENDOZA"));
        hecho4.setUbicacion(ubicacion4);
        hechos.add(hecho4);

        return hechos;
    }
}