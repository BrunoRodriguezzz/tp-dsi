package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.*;
import ar.edu.utn.frba.dds.client.dtos.estadisticas.EstadisticaHoraXCategoriaDTO;
import ar.edu.utn.frba.dds.client.dtos.estadisticas.EstadisticaProvinciaXColeccionDTO;
import ar.edu.utn.frba.dds.client.dtos.estadisticas.EstadisticaSolicitudesDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.solicitud.ResolucionDTO;
import ar.edu.utn.frba.dds.client.dtos.solicitud.SolicitudDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class MockService {
    public List<SolicitudDTO> obtenerSolicitudesMockeadas() {
        return Arrays.asList(
            SolicitudDTO.builder()
                .id(1L)
                .hecho(this.obtenerHechoPorId(1L))
                .fundamento("El hecho reportado es de interés comunitario y merece ser verificado.")
                .fechaCreacion(LocalDate.of(2024, 3, 20).atStartOfDay())
                .contribuyente(null)
                .estado("PENDIENTE")
                .resolucion(null)
                .build(),
            SolicitudDTO.builder()
                .id(2L)
                .hecho(this.obtenerHechoPorId(2L))
                .fundamento("Se solicita revisión del hecho debido a la falta de detalles en la descripción.")
                .fechaCreacion(LocalDate.of(2024, 3, 18).atStartOfDay())
                .contribuyente(this.MockContribuyenteDTO())
                .estado("APROBADA")
                .resolucion(MockResolucionDTO())
                .build(),
            SolicitudDTO.builder()
                .id(3L)
                .hecho(this.obtenerHechoPorId(3L))
                .fundamento("El hecho parece relevante para la comunidad educativa y debe ser verificado.")
                .fechaCreacion(LocalDate.of(2024, 3, 15).atStartOfDay())
                .contribuyente(this.MockContribuyenteDTO())
                .estado("RECHAZADA")
                .resolucion(MockResolucionDTO())
                .build()
        );
    }

    public List<FuenteDTO> obtenerFuentesMockeadas() {
        return Arrays.asList(
            FuenteDTO.builder()
                .id(1L)
                .nombre("Secretaría de Cultura de San Telmo")
                .tipoFuente("MANUAL")
                .cantidadHechos(5)
                .build(),
            FuenteDTO.builder()
                .id(2L)
                .nombre("Policía de Córdoba - Sistema de Emergencias")
                .tipoFuente("DATASET")
                .cantidadHechos(8)
                .build(),
            FuenteDTO.builder()
                .id(3L)
                .nombre("Diario La Capital")
                .tipoFuente("PROXY")
                .cantidadHechos(12)
                .build(),
            FuenteDTO.builder()
                .id(4L)
                .nombre("María González - Vecina del barrio")
                .tipoFuente("CONTRIBUYENTE")
                .cantidadHechos(3)
                .build(),
            FuenteDTO.builder()
                .id(5L)
                .nombre("Liga Deportiva Municipal")
                .tipoFuente("MANUAL")
                .cantidadHechos(7)
                .build(),
            FuenteDTO.builder()
                .id(6L)
                .nombre("ONG Verde Tucumán")
                .tipoFuente("CONTRIBUYENTE")
                .cantidadHechos(4)
                .build(),
            FuenteDTO.builder()
                .id(7L)
                .nombre("Portal de Noticias Mar del Plata Hoy")
                .tipoFuente("PROXY")
                .cantidadHechos(6)
                .build(),
            FuenteDTO.builder()
                .id(8L)
                .nombre("Ministerio de Salud de Salta - Boletín Epidemiológico")
                .tipoFuente("DATASET")
                .cantidadHechos(10)
                .build()
        );
    }

    public List<HechoDTO> obtenerHechosMockeados() {
        return Arrays.asList(
            HechoDTO.builder()
                .id(1L)
                .titulo("Festival de Tango en el Barrio")
                .descripcion("Festival de tango con orquestas locales, milongas al aire libre, clases gratuitas y competencia de baile.")
                .categoria("Cultural")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Buenos Aires")
                    .municipio("San Telmo")
                    .latitud("-34.6206")
                    .longitud("-58.3731")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 3, 15).atStartOfDay())
                .etiquetas(Arrays.asList("tango", "música", "cultura", "barrio"))
                .fuente("Secretaría de Cultura de San Telmo")
                .origen("MANUAL")
                .build(),

            HechoDTO.builder()
                .id(2L)
                .titulo("Accidente de Tránsito en Avenida Principal")
                .descripcion("Colisión múltiple entre tres vehículos, sin heridos graves, pero con demoras en el tráfico.")
                .categoria("Seguridad")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Córdoba")
                    .municipio("Villa Carlos Paz")
                    .latitud("-31.4231")
                    .longitud("-64.4965")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 2, 28).atStartOfDay())
                .etiquetas(Arrays.asList("accidente", "tránsito", "emergencia"))
                .fuente("Policía de Córdoba - Sistema de Emergencias")
                .origen("DATASET")
                .build(),

            HechoDTO.builder()
                .id(3L)
                .titulo("Inauguración de Nueva Biblioteca Municipal")
                .descripcion("Nueva biblioteca con 15,000 libros, salas de estudio, internet gratuito y programas especiales.")
                .categoria("Educación")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Santa Fe")
                    .municipio("Rosario")
                    .latitud("-32.9468")
                    .longitud("-60.6393")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 4, 10).atStartOfDay())
                .etiquetas(Arrays.asList("educación", "biblioteca", "inauguración", "comunidad"))
                .fuente("Diario La Capital")
                .origen("PROXY")
                .build(),

            HechoDTO.builder()
                .id(4L)
                .titulo("Protesta por Mejores Condiciones Laborales")
                .descripcion("Manifestación pacífica de trabajadores municipales por aumento salarial y mejores condiciones.")
                .categoria("Social")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Mendoza")
                    .municipio("Godoy Cruz")
                    .latitud("-32.9267")
                    .longitud("-68.8545")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 1, 22).atStartOfDay())
                .etiquetas(Arrays.asList("protesta", "trabajo", "derechos", "sindical"))
                .fuente("María González - Vecina del barrio")
                .origen("CONTRIBUYENTE")
                .build(),

            HechoDTO.builder()
                .id(5L)
                .titulo("Torneo de Fútbol Juvenil")
                .descripcion("Campeonato de fútbol para jóvenes de 12 a 18 años con 16 equipos de la región.")
                .categoria("Deportes")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Buenos Aires")
                    .municipio("La Plata")
                    .latitud("-34.9206")
                    .longitud("-57.9544")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 5, 8).atStartOfDay())
                .etiquetas(Arrays.asList("fútbol", "juvenil", "deporte", "torneo"))
                .fuente("Liga Deportiva Municipal")
                .origen("MANUAL")
                .build(),

            HechoDTO.builder()
                .id(6L)
                .titulo("Operativo de Limpieza en Plaza Central")
                .descripcion("Jornada comunitaria de limpieza y mantenimiento de la plaza central, con plantación de árboles y renovación de juegos infantiles.")
                .categoria("Medio Ambiente")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Tucumán")
                    .municipio("San Miguel de Tucumán")
                    .latitud("-26.8241")
                    .longitud("-65.2226")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 3, 5).atStartOfDay())
                .etiquetas(Arrays.asList("limpieza", "medio ambiente", "plaza", "comunidad"))
                .fuente("ONG Verde Tucumán")
                .origen("CONTRIBUYENTE")
                .build(),

            HechoDTO.builder()
                .id(7L)
                .titulo("Concierto de Rock Nacional")
                .descripcion("Presentación de bandas de rock nacional en el anfiteatro municipal, con entrada libre y gratuita.")
                .categoria("Entretenimiento")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Buenos Aires")
                    .municipio("Mar del Plata")
                    .latitud("-38.0055")
                    .longitud("-57.5426")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 4, 20).atStartOfDay())
                .etiquetas(Arrays.asList("rock", "música", "concierto", "nacional"))
                .fuente("Portal de Noticias Mar del Plata Hoy")
                .origen("PROXY")
                .build(),

            HechoDTO.builder()
                .id(8L)
                .titulo("Campaña de Vacunación Gratuita")
                .descripcion("Campaña de vacunación contra la gripe estacional para adultos mayores y grupos de riesgo.")
                .categoria("Salud")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Salta")
                    .municipio("Salta Capital")
                    .latitud("-24.7821")
                    .longitud("-65.4232")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 2, 14).atStartOfDay())
                .etiquetas(Arrays.asList("vacunación", "salud", "gratuita", "campaña"))
                .fuente("Ministerio de Salud de Salta - Boletín Epidemiológico")
                .origen("DATASET")
                .build(),

            // Hechos adicionales para probar el carousel
            HechoDTO.builder()
                .id(9L)
                .titulo("Maratón Solidaria")
                .descripcion("Evento deportivo para recaudar fondos para hospitales locales.")
                .categoria("Deportes")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Buenos Aires")
                    .municipio("Quilmes")
                    .latitud("-34.7206")
                    .longitud("-58.2544")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 6, 12).atStartOfDay())
                .etiquetas(Arrays.asList("maratón", "solidaria", "deporte"))
                .fuente("Municipalidad de Quilmes")
                .origen("MANUAL")
                .build(),

            HechoDTO.builder()
                .id(10L)
                .titulo("Feria de Ciencia Escolar")
                .descripcion("Exposición de proyectos científicos de escuelas primarias y secundarias.")
                .categoria("Educación")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Córdoba")
                    .municipio("Córdoba Capital")
                    .latitud("-31.4201")
                    .longitud("-64.1888")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 7, 3).atStartOfDay())
                .etiquetas(Arrays.asList("ciencia", "feria", "escuela"))
                .fuente("Ministerio de Educación de Córdoba")
                .origen("DATASET")
                .build(),

            HechoDTO.builder()
                .id(11L)
                .titulo("Festival Gastronómico Regional")
                .descripcion("Degustación de platos típicos y cocina en vivo con chefs invitados.")
                .categoria("Cultural")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Santa Fe")
                    .municipio("Santa Fe Capital")
                    .latitud("-31.6333")
                    .longitud("-60.7000")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 8, 15).atStartOfDay())
                .etiquetas(Arrays.asList("gastronomía", "festival", "cultura"))
                .fuente("Secretaría de Turismo de Santa Fe")
                .origen("MANUAL")
                .build(),

            HechoDTO.builder()
                .id(12L)
                .titulo("Jornada de Donación de Sangre")
                .descripcion("Campaña de donación voluntaria de sangre en hospitales públicos.")
                .categoria("Salud")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Mendoza")
                    .municipio("Mendoza Capital")
                    .latitud("-32.8908")
                    .longitud("-68.8272")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2024, 9, 10).atStartOfDay())
                .etiquetas(Arrays.asList("donación", "sangre", "salud"))
                .fuente("Hospital Central de Mendoza")
                .origen("CONTRIBUYENTE")
                .build()
        );
    }
    public HechoDTO obtenerHechoPorId(Long id) {
        return this.obtenerHechosMockeados()
            .stream()
            .filter(hecho -> hecho.getId().equals(id))
            .findFirst().orElse(null);
    }

    public List<HechoDTO> obtenerHechosPendientesMockeados() {
        return Arrays.asList(
            HechoDTO.builder()
                .id(101L)
                .titulo("Corte de luz en barrio San Martín")
                .descripcion("Interrupción del suministro eléctrico desde las 22:00 por trabajos de mantenimiento.")
                .categoria("Servicios Públicos")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Buenos Aires")
                    .municipio("San Martín")
                    .latitud("-34.5747")
                    .longitud("-58.5516")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2025, 10, 10).atStartOfDay())
                .etiquetas(Arrays.asList("corte de luz", "mantenimiento", "servicio"))
                .fuente("Empresa de Energía Metropolitana")
                .origen("MANUAL")
                .build(),
            HechoDTO.builder()
                .id(102L)
                .titulo("Feria de emprendedores locales")
                .descripcion("Evento con más de 50 stands de emprendedores y artesanos en la plaza principal.")
                .categoria("Comercial")
                .ubicacion(UbicacionDTO.builder()
                    .provincia("Córdoba")
                    .municipio("Villa Allende")
                    .latitud("-31.2945")
                    .longitud("-64.2956")
                    .build())
                .fechaAcontecimiento(LocalDate.of(2025, 10, 11).atStartOfDay())
                .etiquetas(Arrays.asList("feria", "emprendedores", "artesanos", "evento"))
                .fuente("Municipalidad de Villa Allende")
                .origen("MANUAL")
                .build()
        );
    }

    // Métodos mock para contribuyente y resolución
    private ContribuyenteDTO MockContribuyenteDTO() {
        return ContribuyenteDTO.builder()
            .nombre("Mockeado Contribuyente")
            .build();
    }

    private ResolucionDTO MockResolucionDTO() {
        return ResolucionDTO.builder()
            .administrador(this.MockContribuyenteDTO())
            .fechaResolucion(LocalDate.of(2024, 4, 1).atStartOfDay())
            .build();
    }

    public List<HechoDTO> obtenerHechosMockeadosParaEstadisticas() {
        // Obtenemos los 8 originales
        List<HechoDTO> originales = obtenerHechosMockeados();

        // Siguiente id disponible (arranca en maxId + 1)
        long nextId = originales.stream()
                .mapToLong(HechoDTO::getId)
                .max()
                .orElse(0L) + 1;

        // Hechos adicionales (64) — uso nextId++ para asignar IDs únicos en tiempo de ejecución
        List<HechoDTO> adicionales = Arrays.asList(
                // --- CULTURAL (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Festival de Tango en San Telmo")
                        .descripcion("Espectáculos, milongas y clases en el barrio histórico.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("Buenos Aires").municipio("San Telmo").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 15).atStartOfDay())
                        .etiquetas(Arrays.asList("tango", "milonga", "cultura"))
                        .fuente("Secretaría de Cultura Bs.As.")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Feria del Libro - Córdoba")
                        .descripcion("Feria regional del libro con presentaciones y stands editoriales.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("Córdoba").municipio("Córdoba Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 10).atStartOfDay())
                        .etiquetas(Arrays.asList("libro", "feria", "lectura"))
                        .fuente("Biblioteca Popular Córdoba")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Carnaval del Litoral")
                        .descripcion("Comparsas y desfiles en el centro de la ciudad.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("Corrientes").municipio("Corrientes Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 2, 20).atStartOfDay())
                        .etiquetas(Arrays.asList("carnaval", "desfile", "tradición"))
                        .fuente("Municipalidad de Corrientes")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Festival de Folklore - Merlo")
                        .descripcion("Encuentro nacional de folklore con escuelas y maestros.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("San Luis").municipio("Merlo").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 5).atStartOfDay())
                        .etiquetas(Arrays.asList("folklore", "música", "tradición"))
                        .fuente("Centro Cultural Merlo")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Encuentro de Teatro - Rosario")
                        .descripcion("Muestras de teatro independiente y talleres formativos.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("Santa Fe").municipio("Rosario").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 12).atStartOfDay())
                        .etiquetas(Arrays.asList("teatro", "artes", "comunidad"))
                        .fuente("Red Teatral Santa Fe")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Exposición de Arte Contemporáneo")
                        .descripcion("Muestra con artistas locales y curaduría pública.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("Mendoza").municipio("Mendoza Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 7, 1).atStartOfDay())
                        .etiquetas(Arrays.asList("arte", "exposición", "muestra"))
                        .fuente("Museo Provincial de Arte")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Festival de Cine Regional")
                        .descripcion("Proyecciones y jornadas de cortometrajes.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("Chaco").municipio("Resistencia").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 8, 18).atStartOfDay())
                        .etiquetas(Arrays.asList("cine", "festival", "audiovisual"))
                        .fuente("Cineclub Resistencia")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Festival de Jazz Norteño")
                        .descripcion("Encuentro de jazz con artistas nacionales e invitados internacionales.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder().provincia("Tucumán").municipio("San Miguel de Tucumán").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 9, 3).atStartOfDay())
                        .etiquetas(Arrays.asList("jazz", "música", "festival"))
                        .fuente("Ministerio de Cultura Tucumán")
                        .origen("DATASET")
                        .build(),

                // --- SEGURIDAD (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Accidente en Av. Rivadavia")
                        .descripcion("Colisión múltiple con demoras importantes en la arteria.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Buenos Aires").municipio("La Matanza").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 2, 28).atStartOfDay())
                        .etiquetas(Arrays.asList("accidente", "tránsito"))
                        .fuente("Policía Provincial")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Robo en comercio céntrico")
                        .descripcion("Sustrajeron mercadería y dinero; investigan cámaras.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Córdoba").municipio("Río Cuarto").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 2).atStartOfDay())
                        .etiquetas(Arrays.asList("robo", "comercio", "seguridad"))
                        .fuente("Comisaría Local")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Incendio urbano - depósito controlado")
                        .descripcion("Incendio en depósito industrial; bomberos controlaron sin víctimas.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Salta").municipio("Salta Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 11).atStartOfDay())
                        .etiquetas(Arrays.asList("incendio", "bomberos"))
                        .fuente("Bomberos Voluntarios")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Allanamiento por drogas")
                        .descripcion("Operativo antidrogas con secuestro de estupefacientes.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Jujuy").municipio("San Salvador de Jujuy").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 1, 20).atStartOfDay())
                        .etiquetas(Arrays.asList("narcotráfico", "operativo"))
                        .fuente("Fuerzas de Seguridad")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Ataque a infraestructura de tránsito")
                        .descripcion("Vandalismo en señales y semáforos en la ruta provincial.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Río Negro").municipio("Viedma").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 7).atStartOfDay())
                        .etiquetas(Arrays.asList("vandalismo", "infraestructura"))
                        .fuente("Dirección de Tránsito")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Operativo policial preventivo")
                        .descripcion("Controles vehiculares en accesos a la ciudad.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Neuquén").municipio("Neuquén Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 22).atStartOfDay())
                        .etiquetas(Arrays.asList("control", "prevención"))
                        .fuente("Policía Provincial")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Robo a mano armada en estación de servicio")
                        .descripcion("Delincuentes asaltaron estación; actúan fuerzas de seguridad.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Santa Fe").municipio("Santa Fe Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 30).atStartOfDay())
                        .etiquetas(Arrays.asList("asalto", "delito"))
                        .fuente("Comisaría 5ta")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Entradera frustrada")
                        .descripcion("Vecinos repelieron intento de entradera, dos detenidos.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder().provincia("Santiago del Estero").municipio("Santiago del Estero Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 2).atStartOfDay())
                        .etiquetas(Arrays.asList("delito", "vecinos"))
                        .fuente("Denuncia Vecinal")
                        .origen("CONTRIBUYENTE")
                        .build(),

                // --- EDUCACIÓN (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Inauguración de nueva biblioteca municipal")
                        .descripcion("Apertura con 15.000 libros y salas de estudio.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("Santa Fe").municipio("Rosario").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 10).atStartOfDay())
                        .etiquetas(Arrays.asList("biblioteca", "educación"))
                        .fuente("Diario La Capital")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Entrega de computadoras a escuelas rurales")
                        .descripcion("Programa provincial de conectividad educativa.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("Buenos Aires").municipio("Bahía Blanca").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 20).atStartOfDay())
                        .etiquetas(Arrays.asList("tecnología", "escuelas"))
                        .fuente("Ministerio de Educación")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Curso de formación docente")
                        .descripcion("Capacitación para docentes en nuevas metodologías didácticas.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("Mendoza").municipio("San Rafael").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 15).atStartOfDay())
                        .etiquetas(Arrays.asList("docente", "capacitación"))
                        .fuente("Universidad Provincial")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Refacción de aulas en escuela primaria")
                        .descripcion("Refacción y pintura de seis aulas en escuela pública.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("Entre Ríos").municipio("Paraná").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 12).atStartOfDay())
                        .etiquetas(Arrays.asList("obra", "escuela"))
                        .fuente("Dirección de Obras Públicas")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Programa de alfabetización digital")
                        .descripcion("Talleres gratuitos para adultos mayores.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("San Juan").municipio("San Juan Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 7, 8).atStartOfDay())
                        .etiquetas(Arrays.asList("alfabetización", "digital"))
                        .fuente("ONG Educativa")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Préstamo de libros a domicilio")
                        .descripcion("Iniciativa para zonas rurales con bibliobuses.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("La Pampa").municipio("Santa Rosa").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 8, 1).atStartOfDay())
                        .etiquetas(Arrays.asList("biblioteca", "rural"))
                        .fuente("Servicio Cultural Provincial")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Talleres de ciencia para estudiantes")
                        .descripcion("Actividades experimentales en escuelas secundarias.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("Misiones").municipio("Posadas").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 9, 17).atStartOfDay())
                        .etiquetas(Arrays.asList("ciencia", "taller"))
                        .fuente("Universidad Nacional")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Nuevas salas de estudio en campus")
                        .descripcion("Ampliación de salas y bibliotecas universitarias.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder().provincia("Chubut").municipio("Comodoro Rivadavia").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 10, 5).atStartOfDay())
                        .etiquetas(Arrays.asList("universidad", "infraestructura"))
                        .fuente("Secretaría Universitaria")
                        .origen("CONTRIBUYENTE")
                        .build(),

                // --- SOCIAL (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Protesta por mejores condiciones laborales")
                        .descripcion("Manifestación pacífica por aumento salarial.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("Mendoza").municipio("Godoy Cruz").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 1, 22).atStartOfDay())
                        .etiquetas(Arrays.asList("protesta", "trabajo"))
                        .fuente("Vecinales")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de ayuda alimentaria")
                        .descripcion("Entrega de bolsones a familias en situación vulnerable.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("San Juan").municipio("Pocito").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 2, 18).atStartOfDay())
                        .etiquetas(Arrays.asList("ayuda", "comunidad"))
                        .fuente("Caritas Regional")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Jornada de integración vecinal")
                        .descripcion("Actividades recreativas y talleres comunitarios.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("Formosa").municipio("Formosa Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 28).atStartOfDay())
                        .etiquetas(Arrays.asList("comunidad", "vecinos"))
                        .fuente("Comisión Vecinal")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Asistencia por inundaciones")
                        .descripcion("Entrega de asistencia y reclasificación de viviendas afectadas.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("La Rioja").municipio("La Rioja Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 4).atStartOfDay())
                        .etiquetas(Arrays.asList("inundación", "asistencia"))
                        .fuente("Protección Civil")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Plan de inclusión laboral")
                        .descripcion("Programa para la inserción de jóvenes al mercado laboral.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("Salta").municipio("Salta Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 14).atStartOfDay())
                        .etiquetas(Arrays.asList("empleo", "juventud"))
                        .fuente("Ministerio de Desarrollo")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Entrega de material escolar")
                        .descripcion("Kit escolar para estudiantes de educación primaria.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("Santiago del Estero").municipio("La Banda").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 9).atStartOfDay())
                        .etiquetas(Arrays.asList("educación", "ayuda"))
                        .fuente("Municipalidad")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de vivienda digna")
                        .descripcion("Relevamiento para mejoramiento de viviendas sociales.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("Santa Cruz").municipio("Río Gallegos").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 7, 20).atStartOfDay())
                        .etiquetas(Arrays.asList("vivienda", "social"))
                        .fuente("Programa Provincial")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Apoyo a emprendedores locales")
                        .descripcion("Feria y microcréditos para emprendedores de la zona.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder().provincia("Tierra del Fuego").municipio("Ushuaia").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 8, 2).atStartOfDay())
                        .etiquetas(Arrays.asList("emprendedores", "economía"))
                        .fuente("Agencia de Desarrollo")
                        .origen("DATASET")
                        .build(),

                // --- DEPORTES (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Torneo de Fútbol Juvenil - La Plata")
                        .descripcion("Competencia regional con 16 equipos.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("Buenos Aires").municipio("La Plata").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 8).atStartOfDay())
                        .etiquetas(Arrays.asList("fútbol", "juvenil"))
                        .fuente("Liga Deportiva Municipal")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Maratón provincial")
                        .descripcion("Prueba atlética de 10K y 21K por calles de la ciudad.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("La Pampa").municipio("Santa Rosa").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 1).atStartOfDay())
                        .etiquetas(Arrays.asList("maratón", "deporte"))
                        .fuente("Secretaría de Deportes")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campeonato regional de básquet")
                        .descripcion("Clásico entre equipos de la región.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("Chaco").municipio("Resistencia").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 7, 11).atStartOfDay())
                        .etiquetas(Arrays.asList("básquet", "competencia"))
                        .fuente("Federación Deportiva")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Torneo de hockey femenino")
                        .descripcion("Encuentro intermunicipal con delegaciones juveniles.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("Neuquén").municipio("Centenario").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 8, 14).atStartOfDay())
                        .etiquetas(Arrays.asList("hockey", "deporte"))
                        .fuente("Clubes Unidos")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campeonato de vóley intercolegial")
                        .descripcion("Fase final del torneo escolar.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("Río Negro").municipio("Bariloche").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 9, 10).atStartOfDay())
                        .etiquetas(Arrays.asList("vóley", "escuelas"))
                        .fuente("Coordinación Deportiva")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Torneo de rugby regional")
                        .descripcion("Encuentro de clubes con categorías juveniles y mayores.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("Misiones").municipio("Oberá").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 10, 6).atStartOfDay())
                        .etiquetas(Arrays.asList("rugby", "clubes"))
                        .fuente("Unión Regional")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Ciclismo urbano - circuito nocturno")
                        .descripcion("Evento recreativo para ciclistas de la ciudad.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("Córdoba").municipio("Villa María").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 11, 2).atStartOfDay())
                        .etiquetas(Arrays.asList("ciclismo", "recreación"))
                        .fuente("Club Ciclista")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Encuentro provincial de natación")
                        .descripcion("Competencia de natación con delegaciones escolares.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder().provincia("San Luis").municipio("San Luis Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 12, 1).atStartOfDay())
                        .etiquetas(Arrays.asList("natación", "competencia"))
                        .fuente("Secretaría de Deportes San Luis")
                        .origen("MANUAL")
                        .build(),

                // --- MEDIO AMBIENTE (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Operativo de limpieza en plaza central")
                        .descripcion("Jornada comunitaria con plantación de árboles.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Tucumán").municipio("San Miguel de Tucumán").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 5).atStartOfDay())
                        .etiquetas(Arrays.asList("limpieza", "plaza"))
                        .fuente("ONG Verde Tucumán")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de reforestación")
                        .descripcion("Plantación masiva en zonas periurbanas.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Misiones").municipio("Eldorado").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 20).atStartOfDay())
                        .etiquetas(Arrays.asList("reforestación", "árboles"))
                        .fuente("Instituto Ambiental")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Limpieza de costa ribereña")
                        .descripcion("Voluntarios retiraron residuos de la costa del río.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Santa Cruz").municipio("Río Gallegos").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 3).atStartOfDay())
                        .etiquetas(Arrays.asList("limpieza", "río"))
                        .fuente("Voluntariado Local")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Monitoreo de calidad del aire")
                        .descripcion("Instalación de sensores y publicación de datos abiertos.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Río Negro").municipio("Cipolletti").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 27).atStartOfDay())
                        .etiquetas(Arrays.asList("aire", "monitor"))
                        .fuente("Secretaría Ambiental")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Protección de humedales")
                        .descripcion("Campaña de concientización y restricción de actividades.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Neuquén").municipio("Plottier").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 7, 14).atStartOfDay())
                        .etiquetas(Arrays.asList("humedales", "conservación"))
                        .fuente("ONG Regional")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Control de incendios forestales")
                        .descripcion("Brigadas realizaron tareas de prevención y control.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Chubut").municipio("Trelew").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 8, 29).atStartOfDay())
                        .etiquetas(Arrays.asList("incendio", "prevención"))
                        .fuente("Bomberos Forestales")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Recuperación de áreas verdes urbanas")
                        .descripcion("Renovación y plantación en parques municipales.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Entre Ríos").municipio("Concordia").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 9, 6).atStartOfDay())
                        .etiquetas(Arrays.asList("parque", "recuperación"))
                        .fuente("Municipalidad")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de separación de residuos")
                        .descripcion("Instalación de puntos verdes y educación a vecinos.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder().provincia("Formosa").municipio("Palo Santo").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 10, 21).atStartOfDay())
                        .etiquetas(Arrays.asList("reciclaje", "residuos"))
                        .fuente("Programa Ambiental Provincial")
                        .origen("MANUAL")
                        .build(),

                // --- ENTRETENIMIENTO (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Concierto de Rock Nacional - Anfiteatro")
                        .descripcion("Bandas locales y nacionales en concierto gratuito.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Buenos Aires").municipio("Mar del Plata").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 20).atStartOfDay())
                        .etiquetas(Arrays.asList("rock", "concierto"))
                        .fuente("Portal Noticias MDQ")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Noche de stand up")
                        .descripcion("Comediantes locales presentan función en teatro independiente.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Córdoba").municipio("Córdoba Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 5).atStartOfDay())
                        .etiquetas(Arrays.asList("standup", "teatro"))
                        .fuente("Cartelera Cultural")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Festival gastronómico")
                        .descripcion("Muestras de cocina regional y foodtrucks.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Salta").municipio("Cafayate").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 22).atStartOfDay())
                        .etiquetas(Arrays.asList("gastronomía", "festival"))
                        .fuente("Cámara de Turismo")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Ciclo de cine al aire libre")
                        .descripcion("Proyecciones gratuitas en plaza central.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Mendoza").municipio("Godoy Cruz").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 7, 30).atStartOfDay())
                        .etiquetas(Arrays.asList("cine", "evento"))
                        .fuente("Municipalidad")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Feria de artes y diseño")
                        .descripcion("Emprendedores locales exponen y venden sus productos.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Neuquén").municipio("Plottier").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 8, 11).atStartOfDay())
                        .etiquetas(Arrays.asList("feria", "emprendedores"))
                        .fuente("Red de Emprendedores")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Teatro infantil gratuito")
                        .descripcion("Obra para niños con entrada libre y gratuita.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Santa Fe").municipio("Venado Tuerto").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 9, 9).atStartOfDay())
                        .etiquetas(Arrays.asList("teatro", "infantil"))
                        .fuente("Centro Cultural")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Recital de música electrónica")
                        .descripcion("Evento nocturno en espacio cultural.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Chaco").municipio("Resistencia").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 10, 19).atStartOfDay())
                        .etiquetas(Arrays.asList("música", "electrónica"))
                        .fuente("Promotor Local")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Encuentro de stand up y humor")
                        .descripcion("Varias funciones en distintos bares y teatros.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder().provincia("Chubut").municipio("Puerto Madryn").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 11, 2).atStartOfDay())
                        .etiquetas(Arrays.asList("humor", "standup"))
                        .fuente("Agenda Cultural")
                        .origen("PROXY")
                        .build(),

                // --- SALUD (8) ---
                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de Vacunación Gratuita - Gripe")
                        .descripcion("Vacunación para adultos mayores y grupos de riesgo.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Salta").municipio("Salta Capital").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 2, 14).atStartOfDay())
                        .etiquetas(Arrays.asList("vacunación", "salud"))
                        .fuente("Ministerio de Salud Salta")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de concientización sobre dengue")
                        .descripcion("Tareas de prevención y descacharrado.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Tucumán").municipio("San Miguel de Tucumán").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 1).atStartOfDay())
                        .etiquetas(Arrays.asList("dengue", "prevención"))
                        .fuente("Ministerio de Salud Tucumán")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de salud integral en barrios")
                        .descripcion("Consultorios móviles y atención gratuita.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Buenos Aires").municipio("Lanús").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 18).atStartOfDay())
                        .etiquetas(Arrays.asList("salud", "consultorio móvil"))
                        .fuente("Municipalidad Local")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Jornada de donación de sangre")
                        .descripcion("Campaña organizada por hospitales provinciales.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Córdoba").municipio("Río Tercero").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 25).atStartOfDay())
                        .etiquetas(Arrays.asList("donación", "sangre"))
                        .fuente("Hospital Regional")
                        .origen("MANUAL")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de salud mental en universidades")
                        .descripcion("Talleres y atención psicológica gratuita para estudiantes.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Jujuy").municipio("Perico").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 6, 30).atStartOfDay())
                        .etiquetas(Arrays.asList("salud mental", "universidad"))
                        .fuente("Universidad Provincial")
                        .origen("DATASET")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Control epidemiológico de enfermedades respiratorias")
                        .descripcion("Monitoreo y asesoramiento en centros de salud.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Chubut").municipio("Rawson").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 7, 19).atStartOfDay())
                        .etiquetas(Arrays.asList("epidemiología", "salud pública"))
                        .fuente("Ministerio de Salud Chubut")
                        .origen("PROXY")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Operativo sanitario en zonas rurales")
                        .descripcion("Atención médica y entrega de medicamentos esenciales.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Río Negro").municipio("Allen").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 8, 13).atStartOfDay())
                        .etiquetas(Arrays.asList("salud", "rural"))
                        .fuente("Salud Pública Provincial")
                        .origen("CONTRIBUYENTE")
                        .build(),

                HechoDTO.builder()
                        .id(nextId++).titulo("Campaña de prevención de adicciones")
                        .descripcion("Charlas y talleres destinados a jóvenes y familias.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder().provincia("Santa Fe").municipio("Reconquista").build())
                        .fechaAcontecimiento(LocalDate.of(2024, 9, 27).atStartOfDay())
                        .etiquetas(Arrays.asList("prevención", "adicciones"))
                        .fuente("Programa Provincial de Adicciones")
                        .origen("MANUAL")
                        .build()
        );

        // Unión final: originales primero, luego los adicionales con IDs ya únicos
        List<HechoDTO> resultado = new ArrayList<>(originales.size() + adicionales.size());
        resultado.addAll(originales);
        resultado.addAll(adicionales);
        return resultado;
    }

    public Map<String, Long> getCategorias() {
        Map<String, Map<String, Long>> categoriasPorProvincia = getCategoriasPorProvincia();

        return categoriasPorProvincia.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // la categoría
                        e -> e.getValue().values().stream().mapToLong(Long::longValue).sum() // suma de todas las provincias
                ));
    }

    public Map<String, Map<String, Long>> getCategoriasPorProvincia() {
        List<HechoDTO> hechos = this.obtenerHechosMockeadosParaEstadisticas();

        // Agrupamos: categoría → provincia → cantidad
        Map<String, Map<String, Long>> agrupado = hechos.stream()
                .collect(Collectors.groupingBy(
                        HechoDTO::getCategoria,
                        Collectors.groupingBy(
                                h -> h.getUbicacion().getProvincia(),
                                Collectors.counting()
                        )
                ));

        // Ordenamos provincias dentro de cada categoría
        Map<String, Map<String, Long>> provinciasOrdenadas = agrupado.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().entrySet().stream()
                                .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue())) // descendente
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (a, b) -> a,
                                        LinkedHashMap::new
                                )),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        // Finalmente, ordenamos las categorías por el total de hechos
        return provinciasOrdenadas.entrySet().stream()
                .sorted((c1, c2) -> {
                    long total1 = c1.getValue().values().stream().mapToLong(Long::longValue).sum();
                    long total2 = c2.getValue().values().stream().mapToLong(Long::longValue).sum();
                    return Long.compare(total2, total1); // descendente
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }


    public List<String> getSolicitudes() {
        return Arrays.asList(
                "SPAM",
                "SPAM",
                "ACEPTADA",
                "RECHAZADA",
                "PENDIENTE",
                "ACEPTADA",
                "SPAM",
                "PENDIENTE",
                "ACEPTADA",
                "RECHAZADA",
                "SPAM",
                "SPAM",
                "SPAM"
        );
    }

    public EstadisticaSolicitudesDTO getCantSolicitudesSpam() {
        List<String> solicitudes = this.getSolicitudes();

        // Contar ocurrencias por estado
        Map<String, Long> conteoPorEstado = solicitudes.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        Long cantSpam = conteoPorEstado.getOrDefault("SPAM", 0L);

        // Calcular "no spam"
        Long cantNoSpam = conteoPorEstado.entrySet().stream()
                .filter(e -> !e.getKey().equals("SPAM"))
                .mapToLong(Map.Entry::getValue)
                .sum();

        // Alternativamente podrías usar total - cantSpam:
        // Long cantNoSpam = (long) solicitudes.size() - cantSpam;
        System.out.println("SPam: " + cantSpam);
        System.out.println("NO SPAM: " + cantNoSpam);

        return new EstadisticaSolicitudesDTO(cantSpam, cantNoSpam);
    }

    public List<EstadisticaProvinciaXColeccionDTO> getRankingProvinciasPorColeccion() {
        List<HechoDTO> hechos = this.obtenerHechosMockeadosParaEstadisticas();

        // Agrupar por colección (usamos "categoria" como colección mockeada)
        Map<String, Map<String, Long>> agrupado = hechos.stream()
                .collect(Collectors.groupingBy(
                        HechoDTO::getCategoria,
                        Collectors.groupingBy(
                                h -> h.getUbicacion().getProvincia(),
                                Collectors.counting()
                        )
                ));

        // Convertir a lista de DTOs
        List<EstadisticaProvinciaXColeccionDTO> resultado = new ArrayList<>();

        for (Map.Entry<String, Map<String, Long>> entry : agrupado.entrySet()) {
            String coleccion = entry.getKey();
            Map<String, Long> provincias = entry.getValue();

            // Ordenar provincias por cantidad descendente en LinkedHashMap
            LinkedHashMap<String, Long> provinciasOrdenadas = provincias.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            resultado.add(new EstadisticaProvinciaXColeccionDTO(
                    coleccion,
                    provinciasOrdenadas
            ));
        }

        // Ordenar la lista de DTOs por la suma total de hechos de cada colección (descendente)
        resultado.sort((dto1, dto2) -> {
            long sumaDto1 = dto1.getProvinciasConHechos().values().stream()
                    .mapToLong(Long::longValue)
                    .sum();
            long sumaDto2 = dto2.getProvinciasConHechos().values().stream()
                    .mapToLong(Long::longValue)
                    .sum();
            return Long.compare(sumaDto2, sumaDto1); // Descendente
        });

        System.out.println("\n=== DESPUÉS DEL SORT ===");
        for (EstadisticaProvinciaXColeccionDTO dto : resultado) {
            long suma = dto.getProvinciasConHechos().values().stream()
                    .mapToLong(Long::longValue)
                    .sum();
            System.out.println(dto.getColeccion() + " = " + suma);
        }

        return resultado;
    }

    public List<EstadisticaHoraXCategoriaDTO> getHorariosPorCategoria(Map<String, Long> categorias) {
        // Plantillas: listas de 9 horarios (cada elemento representa 1 hecho).
        Map<String, List<LocalTime>> plantillas = new HashMap<>();

        plantillas.put("Social", Arrays.asList(
                LocalTime.of(6,0), LocalTime.of(6,0),   // pico a la madrugada
                LocalTime.of(14,0), LocalTime.of(14,0), // pico tarde
                LocalTime.of(9,0), LocalTime.of(18,0),
                LocalTime.of(20,0), LocalTime.of(22,0),
                LocalTime.of(11,0)
        ));

        plantillas.put("Educación", Arrays.asList(
                LocalTime.of(9,0), LocalTime.of(9,0),
                LocalTime.of(11,0), LocalTime.of(11,0),
                LocalTime.of(10,0), LocalTime.of(13,0),
                LocalTime.of(14,0), LocalTime.of(15,0),
                LocalTime.of(16,0)
        ));

        plantillas.put("Salud", Arrays.asList(
                LocalTime.of(0,0), LocalTime.of(0,0),  // 2 a medianoche (incluye el original)
                LocalTime.of(6,0),
                LocalTime.of(9,30),
                LocalTime.of(11,15),
                LocalTime.of(14,0),
                LocalTime.of(16,45),
                LocalTime.of(18,0),
                LocalTime.of(20,30)
        ));

        plantillas.put("Deportes", Arrays.asList(
                LocalTime.of(18,0), LocalTime.of(18,0),
                LocalTime.of(19,0), LocalTime.of(20,0),
                LocalTime.of(17,0), LocalTime.of(16,0),
                LocalTime.of(9,0), LocalTime.of(10,0),
                LocalTime.of(11,0)
        ));

        plantillas.put("Seguridad", Arrays.asList(
                LocalTime.of(8,0), LocalTime.of(8,0),
                LocalTime.of(12,0), LocalTime.of(14,0),
                LocalTime.of(16,0), LocalTime.of(18,0),
                LocalTime.of(20,0), LocalTime.of(22,0),
                LocalTime.of(23,0)
        ));

        plantillas.put("Entretenimiento", Arrays.asList(
                LocalTime.of(20,0), LocalTime.of(20,0),
                LocalTime.of(21,0), LocalTime.of(22,0),
                LocalTime.of(23,0), LocalTime.of(19,0),
                LocalTime.of(18,30), LocalTime.of(17,30),
                LocalTime.of(15,0)
        ));

        plantillas.put("Cultural", Arrays.asList(
                LocalTime.of(19,0), LocalTime.of(19,0),
                LocalTime.of(20,30), LocalTime.of(21,0),
                LocalTime.of(18,30), LocalTime.of(17,0),
                LocalTime.of(11,0), LocalTime.of(15,0),
                LocalTime.of(10,0)
        ));

        plantillas.put("Medio Ambiente", Arrays.asList(
                LocalTime.of(8,0), LocalTime.of(8,0),
                LocalTime.of(9,0), LocalTime.of(10,0),
                LocalTime.of(11,0), LocalTime.of(14,0),
                LocalTime.of(15,0), LocalTime.of(16,0),
                LocalTime.of(17,0)
        ));

        // Pool por defecto (si recibís una categoría inesperada)
        List<LocalTime> defecto = Arrays.asList(
                LocalTime.of(9,0), LocalTime.of(11,0), LocalTime.of(14,0),
                LocalTime.of(16,0), LocalTime.of(18,0), LocalTime.of(20,0),
                LocalTime.of(8,0), LocalTime.of(10,0), LocalTime.of(19,0)
        );

        List<EstadisticaHoraXCategoriaDTO> resultado = new ArrayList<>();

        for (Map.Entry<String, Long> e : categorias.entrySet()) {
            String categoria = e.getKey();
            long total = e.getValue() == null ? 0L : e.getValue();

            // Obtener plantilla (lista de horarios con longitud 9). Si total != 9, adaptamos:
            List<LocalTime> plantilla = plantillas.getOrDefault(categoria, defecto);

            // Si el total esperado es distinto de plantilla.size(), ajustamos sencillamente:
            // - si total < plantilla.size(): tomamos los primeros 'total' elementos de la plantilla
            // - si total > plantilla.size(): repetimos la plantilla en ciclo hasta alcanzar total
            List<LocalTime> asignados = new ArrayList<>();
            if (total <= plantilla.size()) {
                for (int i = 0; i < total; i++) asignados.add(plantilla.get(i));
            } else {
                // ciclo
                for (int i = 0; i < total; i++) asignados.add(plantilla.get(i % plantilla.size()));
            }

            // Contar por LocalTime
            Map<LocalTime, Long> horasConHechos = asignados.stream()
                    .collect(Collectors.groupingBy(t -> t, LinkedHashMap::new, Collectors.counting()));

            EstadisticaHoraXCategoriaDTO dto = new EstadisticaHoraXCategoriaDTO();
            dto.setCategoria(categoria);
            dto.setHorasConHechos(horasConHechos);
            resultado.add(dto);
        }

        return resultado;
    }
}
