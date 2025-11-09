package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.AdministradorInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ResolucionSolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.UbicacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Provincia;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GeneradorDatosPrueba {
  /**
   * Genera una lista de colecciones con hechos para testing
   */
  public static List<ColeccionInputDTO> generarColecciones() {
    List<ColeccionInputDTO> colecciones = buscarColeccionesConHechos();
    return colecciones;
//
//        List<ColeccionInputDTO> colecciones = new ArrayList<>();
//
//        // === COLECCION 1: "Hechos Historicos Argentinos" ===
//        ColeccionInputDTO coleccion1 = new ColeccionInputDTO();
//        coleccion1.setId(1L);
//        coleccion1.setTitulo("Hechos Historicos Argentinos");
//        coleccion1.setDescripcion("Eventos historicos importantes de Argentina");
//
//        List<HechoInputDTO> hechos1 = new ArrayList<>();
//
//        // Hecho 1.1: BUENOS_AIRES - Historico - H10
//        HechoInputDTO hecho1_1 = new HechoInputDTO();
//        hecho1_1.setId(1L);
//        hecho1_1.setTitulo("Revolucion de Mayo");
//        hecho1_1.setCategoria("Historico");
//        hecho1_1.setFechaAcontecimiento(LocalDateTime.of(1810, 5, 25, 10, 0));
//        UbicacionInputDTO ubicacion1_1 = new UbicacionInputDTO();
//        ubicacion1_1.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho1_1.setUbicacion(ubicacion1_1);
//        hechos1.add(hecho1_1);
//
//        // Hecho 1.2: BUENOS_AIRES - Historico - H10 (misma clave que 1.1)
//        HechoInputDTO hecho1_2 = new HechoInputDTO();
//        hecho1_2.setId(2L);
//        hecho1_2.setTitulo("Independencia Argentina");
//        hecho1_2.setCategoria("Historico");
//        hecho1_2.setFechaAcontecimiento(LocalDateTime.of(1816, 7, 9, 10, 30));
//        UbicacionInputDTO ubicacion1_2 = new UbicacionInputDTO();
//        ubicacion1_2.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho1_2.setUbicacion(ubicacion1_2);
//        hechos1.add(hecho1_2);
//
//        // Hecho 1.3: CORDOBA - Politico - H14
//        HechoInputDTO hecho1_3 = new HechoInputDTO();
//        hecho1_3.setId(3L);
//        hecho1_3.setTitulo("Cordobazo");
//        hecho1_3.setCategoria("Politico");
//        hecho1_3.setFechaAcontecimiento(LocalDateTime.of(1969, 5, 29, 14, 0));
//        UbicacionInputDTO ubicacion1_3 = new UbicacionInputDTO();
//        ubicacion1_3.setProvincia(Provincia.valueOf("CORDOBA"));
//        hecho1_3.setUbicacion(ubicacion1_3);
//        hechos1.add(hecho1_3);
//
//        // Hecho 1.4: SANTA_FE - Cultural - H09
//        HechoInputDTO hecho1_4 = new HechoInputDTO();
//        hecho1_4.setId(4L);
//        hecho1_4.setTitulo("Fundacion de Santa Fe");
//        hecho1_4.setCategoria("Cultural");
//        hecho1_4.setFechaAcontecimiento(LocalDateTime.of(1573, 11, 15, 9, 0));
//        UbicacionInputDTO ubicacion1_4 = new UbicacionInputDTO();
//        ubicacion1_4.setProvincia(Provincia.valueOf("SANTA_FE"));
//        hecho1_4.setUbicacion(ubicacion1_4);
//        hechos1.add(hecho1_4);
//
//        // Hecho 1.5: BUENOS_AIRES - Cultural - H10
//        HechoInputDTO hecho1_5 = new HechoInputDTO();
//        hecho1_5.setId(8L);
//        hecho1_5.setTitulo("Inauguración del Teatro Colón");
//        hecho1_5.setCategoria("Cultural");
//        hecho1_5.setFechaAcontecimiento(LocalDateTime.of(1908, 5, 25, 20, 0));
//        UbicacionInputDTO ubicacion1_5 = new UbicacionInputDTO();
//        ubicacion1_5.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho1_5.setUbicacion(ubicacion1_5);
//        hechos1.add(hecho1_5);
//
//        coleccion1.setHechos(hechos1);
//        colecciones.add(coleccion1);
//
//        // === COLECCION 2: "Eventos Culturales" ===
//        ColeccionInputDTO coleccion2 = new ColeccionInputDTO();
//        coleccion2.setId(500L);
//        coleccion2.setTitulo("Eventos Culturales");
//        coleccion2.setDescripcion("Importantes eventos culturales del pais");
//
//        List<HechoInputDTO> hechos2 = new ArrayList<>();
//
//        // Hecho 2.1: BUENOS_AIRES - Cultural - H20
//        HechoInputDTO hecho2_1 = new HechoInputDTO();
//        hecho2_1.setId(5L);
//        hecho2_1.setTitulo("Inauguracion Teatro Colon");
//        hecho2_1.setCategoria("Cultural");
//        hecho2_1.setFechaAcontecimiento(LocalDateTime.of(1908, 5, 25, 20, 0));
//        UbicacionInputDTO ubicacion2_1 = new UbicacionInputDTO();
//        ubicacion2_1.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho2_1.setUbicacion(ubicacion2_1);
//        hechos2.add(hecho2_1);
//
//        // Hecho 2.2: CORDOBA - Cultural - H18
//        HechoInputDTO hecho2_2 = new HechoInputDTO();
//        hecho2_2.setId(6L);
//        hecho2_2.setTitulo("Festival de Cosquin");
//        hecho2_2.setCategoria("Cultural");
//        hecho2_2.setFechaAcontecimiento(LocalDateTime.of(1961, 1, 21, 18, 0));
//        UbicacionInputDTO ubicacion2_2 = new UbicacionInputDTO();
//        ubicacion2_2.setProvincia(Provincia.valueOf("CORDOBA"));
//        hecho2_2.setUbicacion(ubicacion2_2);
//        hechos2.add(hecho2_2);
//
//        // Hecho 2.3: BUENOS_AIRES - Historico - H12
//        HechoInputDTO hecho2_3 = new HechoInputDTO();
//        hecho2_3.setId(7L);
//        hecho2_3.setTitulo("Fundacion de Buenos Aires");
//        hecho2_3.setCategoria("Historico");
//        hecho2_3.setFechaAcontecimiento(LocalDateTime.of(1536, 2, 3, 12, 0));
//        UbicacionInputDTO ubicacion2_3 = new UbicacionInputDTO();
//        ubicacion2_3.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho2_3.setUbicacion(ubicacion2_3);
//        hechos2.add(hecho2_3);
//
//        coleccion2.setHechos(hechos2);
//        colecciones.add(coleccion2);
//
//        return colecciones;
  }

  /**
   * Genera una lista de hechos independientes (sin coleccion) para testing
   */
  public static List<HechoInputDTO> generarHechosIndependientes() {
    List<HechoInputDTO> hechos = buscarHechosIndependientes();
    return hechos;
//
//        List<HechoInputDTO> hechos = new ArrayList<>();
//
//        // Hecho independiente 1: MENDOZA - Politico - H06
//        HechoInputDTO hecho1 = new HechoInputDTO();
//        hecho1.setId(101L);
//        hecho1.setTitulo("Cruce de los Andes");
//        hecho1.setCategoria("Politico");
//        hecho1.setFechaAcontecimiento(LocalDateTime.of(1817, 1, 18, 6, 0));
//        UbicacionInputDTO ubicacion1 = new UbicacionInputDTO();
//        ubicacion1.setProvincia(Provincia.valueOf("MENDOZA"));
//        hecho1.setUbicacion(ubicacion1);
//        hechos.add(hecho1);
//
//        // Hecho independiente 2: BUENOS_AIRES - Cultural - H22
//        HechoInputDTO hecho2 = new HechoInputDTO();
//        hecho2.setId(102L);
//        hecho2.setTitulo("Primer Tango");
//        hecho2.setCategoria("Cultural");
//        hecho2.setFechaAcontecimiento(LocalDateTime.of(1880, 12, 1, 22, 0));
//        UbicacionInputDTO ubicacion2 = new UbicacionInputDTO();
//        ubicacion2.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho2.setUbicacion(ubicacion2);
//        hechos.add(hecho2);
//
//        // Hecho independiente 3: SANTA_FE - Historico - H15
//        HechoInputDTO hecho3 = new HechoInputDTO();
//        hecho3.setId(103L);
//        hecho3.setTitulo("Constitucion Nacional");
//        hecho3.setCategoria("Historico");
//        hecho3.setFechaAcontecimiento(LocalDateTime.of(1853, 5, 1, 15, 0));
//        UbicacionInputDTO ubicacion3 = new UbicacionInputDTO();
//        ubicacion3.setProvincia(Provincia.valueOf("SANTA_FE"));
//        hecho3.setUbicacion(ubicacion3);
//        hechos.add(hecho3);
//
//        // Hecho independiente 4: MENDOZA - Politico - H06 (misma clave que hecho1)
//        HechoInputDTO hecho4 = new HechoInputDTO();
//        hecho4.setId(104L);
//        hecho4.setTitulo("Campaña Libertadora");
//        hecho4.setCategoria("Politico");
//        hecho4.setFechaAcontecimiento(LocalDateTime.of(1817, 2, 12, 6, 30));
//        UbicacionInputDTO ubicacion4 = new UbicacionInputDTO();
//        ubicacion4.setProvincia(Provincia.valueOf("MENDOZA"));
//        hecho4.setUbicacion(ubicacion4);
//        hechos.add(hecho4);
//
//        return hechos;
  }

  /**
   * Genera una lista de solicitudes de eliminación para testing
   * Solo incluye estados PENDIENTE y SPAM según los requerimientos
   */
  public static List<SolicitudEliminacionInputDTO> generarSolicitudesInputDTO() {
    List<SolicitudEliminacionInputDTO> solicitudes = buscarSolicitudesEliminacion();
    solicitudes = solicitudes.stream().filter(s -> !s.getEstado().equals("PENDIENTE")).toList();
    return solicitudes;
//
//        List<SolicitudEliminacionInputDTO> solicitudes = new ArrayList<>();
//
//        // === SOLICITUD 1: PENDIENTE - Hecho "Revolucion de Mayo" ===
//        SolicitudEliminacionInputDTO solicitud1 = new SolicitudEliminacionInputDTO();
//        solicitud1.setId(1001L);
//
//        // Crear hecho asociado (Revolución de Mayo)
//        HechoInputDTO hecho1 = new HechoInputDTO();
//        hecho1.setId(1L);
//        hecho1.setTitulo("Revolucion de Mayo");
//        hecho1.setCategoria("Historico");
//        hecho1.setFechaAcontecimiento(LocalDateTime.of(1810, 5, 25, 10, 0));
//        UbicacionInputDTO ubicacion1 = new UbicacionInputDTO();
//        ubicacion1.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho1.setUbicacion(ubicacion1);
//        solicitud1.setHecho(hecho1);
//
//        solicitud1.setFundamento("La información sobre este evento histórico contiene datos incorrectos y no está debidamente fundamentada en fuentes confiables.");
//        solicitud1.setFechaCreacion(LocalDateTime.of(2024, 3, 15, 10, 30));
//        solicitud1.setEstado("PENDIENTE");
//
//        // Contribuyente
//        ContribuyenteInputDTO contribuyente1 = new ContribuyenteInputDTO();
//        contribuyente1.setNombre("Maria");
//        contribuyente1.setApellido("Rodriguez");
//        contribuyente1.setFechaNacimiento(LocalDate.of(1985, 8, 20));
//        solicitud1.setContribuyente(contribuyente1);
//
//        // Sin resolución porque está PENDIENTE
//        solicitud1.setResolucion(null);
//
//        solicitudes.add(solicitud1);
//
//        // === SOLICITUD 2: SPAM - Hecho "Cordobazo" ===
//        SolicitudEliminacionInputDTO solicitud2 = new SolicitudEliminacionInputDTO();
//        solicitud2.setId(1002L);
//
//        // Crear hecho asociado (Cordobazo)
//        HechoInputDTO hecho2 = new HechoInputDTO();
//        hecho2.setId(3L);
//        hecho2.setTitulo("Cordobazo");
//        hecho2.setCategoria("Politico");
//        hecho2.setFechaAcontecimiento(LocalDateTime.of(1969, 5, 29, 14, 0));
//        UbicacionInputDTO ubicacion2 = new UbicacionInputDTO();
//        ubicacion2.setProvincia(Provincia.valueOf("CORDOBA"));
//        hecho2.setUbicacion(ubicacion2);
//        solicitud2.setHecho(hecho2);
//
//        solicitud2.setFundamento("eliminar todo mal contenido!!!!");
//        solicitud2.setFechaCreacion(LocalDateTime.of(2024, 3, 20, 15, 45));
//        solicitud2.setEstado("SPAM");
//
//        // Contribuyente
//        ContribuyenteInputDTO contribuyente2 = new ContribuyenteInputDTO();
//        contribuyente2.setNombre("Juan");
//        contribuyente2.setApellido("Troll");
//        contribuyente2.setFechaNacimiento(LocalDate.of(1995, 12, 25));
//        solicitud2.setContribuyente(contribuyente2);
//
//        // Resolución para SPAM
//        ResolucionSolicitudEliminacionInputDTO resolucion2 = new ResolucionSolicitudEliminacionInputDTO();
//        AdministradorInputDTO admin2 = new AdministradorInputDTO();
//        admin2.setNombre("Carlos");
//        admin2.setApellido("Moderador");
//        resolucion2.setAdministrador(admin2);
//        resolucion2.setFechaResolucion(LocalDateTime.of(2024, 3, 21, 9, 15));
//        solicitud2.setResolucion(resolucion2);
//
//        solicitudes.add(solicitud2);
//
//        // === SOLICITUD 3: PENDIENTE - Hecho "Inauguracion Teatro Colon" ===
//        SolicitudEliminacionInputDTO solicitud3 = new SolicitudEliminacionInputDTO();
//        solicitud3.setId(1003L);
//
//        // Crear hecho asociado (Teatro Colón)
//        HechoInputDTO hecho3 = new HechoInputDTO();
//        hecho3.setId(5L);
//        hecho3.setTitulo("Inauguracion Teatro Colon");
//        hecho3.setCategoria("Cultural");
//        hecho3.setFechaAcontecimiento(LocalDateTime.of(1908, 5, 25, 20, 0));
//        UbicacionInputDTO ubicacion3 = new UbicacionInputDTO();
//        ubicacion3.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho3.setUbicacion(ubicacion3);
//        solicitud3.setHecho(hecho3);
//
//        solicitud3.setFundamento("La fecha de inauguración del Teatro Colón que figura en este registro no coincide con los documentos oficiales del teatro. Solicito la corrección o eliminación.");
//        solicitud3.setFechaCreacion(LocalDateTime.of(2024, 4, 1, 14, 20));
//        solicitud3.setEstado("PENDIENTE");
//
//        // Contribuyente
//        ContribuyenteInputDTO contribuyente3 = new ContribuyenteInputDTO();
//        contribuyente3.setNombre("Ana");
//        contribuyente3.setApellido("Historiadora");
//        contribuyente3.setFechaNacimiento(LocalDate.of(1978, 6, 10));
//        solicitud3.setContribuyente(contribuyente3);
//
//        // Sin resolución porque está PENDIENTE
//        solicitud3.setResolucion(null);
//
//        solicitudes.add(solicitud3);
//
//        // === SOLICITUD 4: SPAM - Hecho "Cruce de los Andes" ===
//        SolicitudEliminacionInputDTO solicitud4 = new SolicitudEliminacionInputDTO();
//        solicitud4.setId(1004L);
//
//        // Crear hecho asociado (Cruce de los Andes)
//        HechoInputDTO hecho4 = new HechoInputDTO();
//        hecho4.setId(101L);
//        hecho4.setTitulo("Cruce de los Andes");
//        hecho4.setCategoria("Politico");
//        hecho4.setFechaAcontecimiento(LocalDateTime.of(1817, 1, 18, 6, 0));
//        UbicacionInputDTO ubicacion4 = new UbicacionInputDTO();
//        ubicacion4.setProvincia(Provincia.valueOf("MENDOZA"));
//        hecho4.setUbicacion(ubicacion4);
//        solicitud4.setHecho(hecho4);
//
//        solicitud4.setFundamento("BORRAR!!!! NO ME GUSTA");
//        solicitud4.setFechaCreacion(LocalDateTime.of(2024, 4, 5, 11, 0));
//        solicitud4.setEstado("SPAM");
//
//        // Contribuyente
//        ContribuyenteInputDTO contribuyente4 = new ContribuyenteInputDTO();
//        contribuyente4.setNombre("Pedro");
//        contribuyente4.setApellido("Spammer");
//        contribuyente4.setFechaNacimiento(LocalDate.of(2000, 1, 1));
//        solicitud4.setContribuyente(contribuyente4);
//
//        // Resolución para SPAM
//        ResolucionSolicitudEliminacionInputDTO resolucion4 = new ResolucionSolicitudEliminacionInputDTO();
//        AdministradorInputDTO admin4 = new AdministradorInputDTO();
//        admin4.setNombre("Laura");
//        admin4.setApellido("Supervisora");
//        resolucion4.setAdministrador(admin4);
//        resolucion4.setFechaResolucion(LocalDateTime.of(2024, 4, 5, 16, 30));
//        solicitud4.setResolucion(resolucion4);
//
//        solicitudes.add(solicitud4);
//
//        // === SOLICITUD 5: PENDIENTE - Hecho "Primer Tango" ===
//        SolicitudEliminacionInputDTO solicitud5 = new SolicitudEliminacionInputDTO();
//        solicitud5.setId(1005L);
//
//        // Crear hecho asociado (Primer Tango)
//        HechoInputDTO hecho5 = new HechoInputDTO();
//        hecho5.setId(102L);
//        hecho5.setTitulo("Primer Tango");
//        hecho5.setCategoria("Cultural");
//        hecho5.setFechaAcontecimiento(LocalDateTime.of(1880, 12, 1, 22, 0));
//        UbicacionInputDTO ubicacion5 = new UbicacionInputDTO();
//        ubicacion5.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho5.setUbicacion(ubicacion5);
//        solicitud5.setHecho(hecho5);
//
//        solicitud5.setFundamento("Como investigador del tango, considero que la información sobre el 'primer tango' es imprecisa. Los orígenes del tango son difusos y no se puede precisar un momento exacto de su creación.");
//        solicitud5.setFechaCreacion(LocalDateTime.of(2024, 4, 10, 9, 45));
//        solicitud5.setEstado("PENDIENTE");
//
//        // Contribuyente
//        ContribuyenteInputDTO contribuyente5 = new ContribuyenteInputDTO();
//        contribuyente5.setNombre("Roberto");
//        contribuyente5.setApellido("Musicologo");
//        contribuyente5.setFechaNacimiento(LocalDate.of(1965, 11, 3));
//        solicitud5.setContribuyente(contribuyente5);
//
//        // Sin resolución porque está PENDIENTE
//        solicitud5.setResolucion(null);
//
//        solicitudes.add(solicitud5);
//
//        // === SOLICITUD 6: SPAM - Hecho "Fundacion de Santa Fe" ===
//        SolicitudEliminacionInputDTO solicitud6 = new SolicitudEliminacionInputDTO();
//        solicitud6.setId(1006L);
//
//        // Crear hecho asociado (Fundación de Santa Fe)
//        HechoInputDTO hecho6 = new HechoInputDTO();
//        hecho6.setId(4L);
//        hecho6.setTitulo("Fundacion de Santa Fe");
//        hecho6.setCategoria("Cultural");
//        hecho6.setFechaAcontecimiento(LocalDateTime.of(1573, 11, 15, 9, 0));
//        UbicacionInputDTO ubicacion6 = new UbicacionInputDTO();
//        ubicacion6.setProvincia(Provincia.valueOf("SANTA_FE"));
//        hecho6.setUbicacion(ubicacion6);
//        solicitud6.setHecho(hecho6);
//
//        solicitud6.setFundamento("Santa Fe es aburrida, eliminen esto");
//        solicitud6.setFechaCreacion(LocalDateTime.of(2024, 4, 12, 13, 20));
//        solicitud6.setEstado("SPAM");
//
//        // Contribuyente
//        ContribuyenteInputDTO contribuyente6 = new ContribuyenteInputDTO();
//        contribuyente6.setNombre("Anonimo");
//        contribuyente6.setApellido("Usuario");
//        contribuyente6.setFechaNacimiento(LocalDate.of(1990, 5, 15));
//        solicitud6.setContribuyente(contribuyente6);
//
//        // Resolución para SPAM
//        ResolucionSolicitudEliminacionInputDTO resolucion6 = new ResolucionSolicitudEliminacionInputDTO();
//        AdministradorInputDTO admin6 = new AdministradorInputDTO();
//        admin6.setNombre("Patricia");
//        admin6.setApellido("Administradora");
//        resolucion6.setAdministrador(admin6);
//        resolucion6.setFechaResolucion(LocalDateTime.of(2024, 4, 12, 17, 45));
//        solicitud6.setResolucion(resolucion6);
//
//        solicitudes.add(solicitud6);
//
//        // === SOLICITUD 7: RECHAZADA - Hecho "Inauguración Obelisco" ===
//        SolicitudEliminacionInputDTO solicitud7 = new SolicitudEliminacionInputDTO();
//        solicitud7.setId(1007L);
//
//        // Crear hecho asociado (Inauguración del Obelisco)
//        HechoInputDTO hecho7 = new HechoInputDTO();
//        hecho7.setId(8L);
//        hecho7.setTitulo("Inauguración del Obelisco");
//        hecho7.setCategoria("Histórico");
//        hecho7.setFechaAcontecimiento(LocalDateTime.of(1936, 5, 23, 12, 0));
//        UbicacionInputDTO ubicacion7 = new UbicacionInputDTO();
//        ubicacion7.setProvincia(Provincia.valueOf("BUENOS_AIRES"));
//        hecho7.setUbicacion(ubicacion7);
//        solicitud7.setHecho(hecho7);
//
//        solicitud7.setFundamento("El obelisco no representa a todos, debería eliminarse");
//        solicitud7.setFechaCreacion(LocalDateTime.of(2024, 5, 10, 10, 30));
//        solicitud7.setEstado("RECHAZADA");
//
//        // Contribuyente
//        ContribuyenteInputDTO contribuyente7 = new ContribuyenteInputDTO();
//        contribuyente7.setNombre("Carlos");
//        contribuyente7.setApellido("Gómez");
//        contribuyente7.setFechaNacimiento(LocalDate.of(1985, 3, 22));
//        solicitud7.setContribuyente(contribuyente7);
//
//        // Resolución para RECHAZADA
//        ResolucionSolicitudEliminacionInputDTO resolucion7 = new ResolucionSolicitudEliminacionInputDTO();
//        AdministradorInputDTO admin7 = new AdministradorInputDTO();
//        admin7.setNombre("Lucía");
//        admin7.setApellido("Moderadora");
//        resolucion7.setAdministrador(admin7);
//        resolucion7.setFechaResolucion(LocalDateTime.of(2024, 5, 11, 14, 15));
//        solicitud7.setResolucion(resolucion7);
//
//        solicitudes.add(solicitud7);
//
//        return solicitudes;
  }
    public static List<ColeccionInputDTO> buscarColeccionesConHechos() {
      WebClient webClient = WebClient.builder()
          .baseUrl("http://localhost:8082")
          .build();

      try {
        List<ColeccionInputDTO> colecciones = obtenerTodasLasColeccionesSimple(webClient);
        List<ColeccionInputDTO> coleccionesCompletas = Flux.fromIterable(colecciones)
            .flatMap(coleccion -> obtenerTodosLosHechosPorColeccion(webClient, coleccion.getId())
                .collectList()
                .map(hechos -> {
                  coleccion.setHechos(hechos);
                  return coleccion;
                }))
            .collectList()
            .block();

        return coleccionesCompletas != null ? coleccionesCompletas : new ArrayList<>();

      } catch (Exception e) {
        System.err.println("Error al buscar colecciones con hechos: " + e.getMessage());
        return new ArrayList<>();
      }
    }

    private static List<ColeccionInputDTO> obtenerTodasLasColeccionesSimple(WebClient webClient) {
      try {
        String response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/colecciones")
                .queryParam("all", true)
                .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonResponse = mapper.readTree(response);

        List<ColeccionInputDTO> colecciones = mapper.readValue(
            jsonResponse.get("content").toString(),
            new TypeReference<List<ColeccionInputDTO>>() {
            }
        );

        return colecciones;

      } catch (Exception e) {
        System.err.println("Error al obtener todas las colecciones: " + e.getMessage());
        return new ArrayList<>();
      }
    }

    private static Flux<HechoInputDTO> obtenerTodosLosHechosPorColeccion(WebClient webClient, Long coleccionId) {
      return webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/colecciones/{id}/hechos")
              .queryParam("all", true)
              .build(coleccionId))
          .retrieve()
          .bodyToMono(String.class)
          .map(response -> {
            try {
              ObjectMapper mapper = new ObjectMapper();
              mapper.registerModule(new JavaTimeModule());
              JsonNode jsonResponse = mapper.readTree(response);

              List<HechoInputDTO> hechos = mapper.readValue(
                  jsonResponse.get("content").toString(),
                  new TypeReference<List<HechoInputDTO>>() {
                  }
              );

              return hechos;

            } catch (Exception e) {
              throw new RuntimeException("Error parsing hechos para colección " + coleccionId, e);
            }
          })
          .onErrorReturn(new ArrayList<>())
          .flatMapMany(Flux::fromIterable);
    }

    public static List<HechoInputDTO> buscarHechosIndependientes() {
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();

        try {
          String response = webClient.get()
              .uri("/hechos/independientes")
              .retrieve()
              .bodyToMono(String.class)
              .block();

          ObjectMapper mapper = new ObjectMapper();
          mapper.registerModule(new JavaTimeModule());

          List<HechoInputDTO> hechos = mapper.readValue(
              response,
              new TypeReference<List<HechoInputDTO>>() {
              }
          );

          return hechos;

        } catch (Exception e) {
          System.err.println("Error al buscar hechos independientes: " + e.getMessage());
          return new ArrayList<>();
        }
    }

    public static List<SolicitudEliminacionInputDTO> buscarSolicitudesEliminacion() {
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();

        try {
          String response = webClient.get()
              .uri("/solicitudesEliminacion")
              .retrieve()
              .bodyToMono(String.class)
              .block();

          ObjectMapper mapper = new ObjectMapper();
          mapper.registerModule(new JavaTimeModule());

          List<SolicitudEliminacionInputDTO> solicitudes = mapper.readValue(
              response,
              new TypeReference<List<SolicitudEliminacionInputDTO>>() {
              }
          );

          return solicitudes;

        } catch (Exception e) {
          System.err.println("Error al buscar solicitudes de eliminación: " + e.getMessage());
          return new ArrayList<>();
        }
    }
}