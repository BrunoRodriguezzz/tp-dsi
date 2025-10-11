package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MockService {

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
                .fechaAcontecimiento(LocalDate.of(2024, 3, 15))
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
                .fechaAcontecimiento(LocalDate.of(2024, 2, 28))
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
                .fechaAcontecimiento(LocalDate.of(2024, 4, 10))
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
                .fechaAcontecimiento(LocalDate.of(2024, 1, 22))
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
                .fechaAcontecimiento(LocalDate.of(2024, 5, 8))
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
                .fechaAcontecimiento(LocalDate.of(2024, 3, 5))
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
                .fechaAcontecimiento(LocalDate.of(2024, 4, 20))
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
                .fechaAcontecimiento(LocalDate.of(2024, 2, 14))
                .etiquetas(Arrays.asList("vacunación", "salud", "gratuita", "campaña"))
                .fuente("Ministerio de Salud de Salta - Boletín Epidemiológico")
                .origen("DATASET")
                .build()
        );
    }
    public HechoDTO obtenerHechoPorId(Long id) {
        return this.obtenerHechosMockeados()
            .stream()
            .filter(hecho -> hecho.getId().equals(id))
            .findFirst().get();
    }

    public Map<String, Long> getCategoriaTop() {
        List<HechoDTO> hechos = this.obtenerHechosMockeados();
        // Agrupar por categoría (String) y contar
        return hechos.stream()
                .collect(Collectors.groupingBy(
                        HechoDTO::getCategoria,
                        Collectors.counting()
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
                "RECHAZADA"
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

        return new EstadisticaSolicitudesDTO(LocalDateTime.now(), cantSpam, cantNoSpam);
    }
}
