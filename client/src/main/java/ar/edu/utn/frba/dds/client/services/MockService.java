package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.UbicacionDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MockService {

    public List<HechoDTO> obtenerHechosMockeados() {
        return Arrays.asList(
                HechoDTO.builder()
                        .titulo("Festival de Tango en el Barrio")
                        .descripcion("Gran festival de tango con presentaciones de orquestas locales y milongas al aire libre. El evento incluye clases gratuitas para principiantes y una competencia de baile con premios para los ganadores.")
                        .categoria("Cultural")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Buenos Aires")
                                .municipio("San Telmo")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 15))
                        .etiquetas(Arrays.asList("tango", "música", "cultura", "barrio"))
                        .build(),

                HechoDTO.builder()
                        .titulo("Accidente de Tránsito en Avenida Principal")
                        .descripcion("Colisión múltiple entre tres vehículos en la intersección de Avenida San Martín y Belgrano. No hubo heridos de gravedad, pero se registraron importantes demoras en el tráfico durante dos horas.")
                        .categoria("Seguridad")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Córdoba")
                                .municipio("Villa Carlos Paz")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 2, 28))
                        .etiquetas(Arrays.asList("accidente", "tránsito", "emergencia"))
                        .build(),

                HechoDTO.builder()
                        .titulo("Inauguración de Nueva Biblioteca Municipal")
                        .descripcion("Se inauguró la nueva biblioteca municipal con una colección de más de 15,000 libros, salas de estudio, acceso a internet gratuito y programas especiales para niños y adultos mayores.")
                        .categoria("Educación")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Santa Fe")
                                .municipio("Rosario")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 10))
                        .etiquetas(Arrays.asList("educación", "biblioteca", "inauguración", "comunidad"))
                        .build(),

                HechoDTO.builder()
                        .titulo("Protesta por Mejores Condiciones Laborales")
                        .descripcion("Manifestación pacífica de trabajadores municipales reclamando aumento salarial y mejores condiciones de trabajo. La protesta se realizó frente al edificio municipal con la participación de aproximadamente 200 personas.")
                        .categoria("Social")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Mendoza")
                                .municipio("Godoy Cruz")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 1, 22))
                        .etiquetas(Arrays.asList("protesta", "trabajo", "derechos", "sindical"))
                        .build(),

                HechoDTO.builder()
                        .titulo("Torneo de Fútbol Juvenil")
                        .descripcion("Campeonato de fútbol para jóvenes de 12 a 18 años con la participación de 16 equipos de la región. El torneo se disputará durante todo el mes con partidos los fines de semana en el estadio municipal.")
                        .categoria("Deportes")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Buenos Aires")
                                .municipio("La Plata")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 5, 8))
                        .etiquetas(Arrays.asList("fútbol", "juvenil", "deporte", "torneo"))
                        .build(),

                HechoDTO.builder()
                        .titulo("Operativo de Limpieza en Plaza Central")
                        .descripcion("Jornada comunitaria de limpieza y mantenimiento de la plaza central con la participación de vecinos y organizaciones locales. Se plantaron nuevos árboles y se renovaron los juegos infantiles.")
                        .categoria("Medio Ambiente")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Tucumán")
                                .municipio("San Miguel de Tucumán")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 3, 5))
                        .etiquetas(Arrays.asList("limpieza", "medio ambiente", "plaza", "comunidad"))
                        .build(),

                HechoDTO.builder()
                        .titulo("Concierto de Rock Nacional")
                        .descripcion("Presentación de bandas de rock nacional en el anfiteatro municipal. El evento contó con la participación de cinco bandas locales y dos grupos invitados de Buenos Aires, con entrada libre y gratuita.")
                        .categoria("Entretenimiento")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Buenos Aires")
                                .municipio("Mar del Plata")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 4, 20))
                        .etiquetas(Arrays.asList("rock", "música", "concierto", "nacional"))
                        .build(),

                HechoDTO.builder()
                        .titulo("Campaña de Vacunación Gratuita")
                        .descripcion("Campaña municipal de vacunación gratuita contra la gripe estacional dirigida especialmente a adultos mayores y grupos de riesgo. Se habilitaron cinco centros de vacunación en diferentes barrios de la ciudad.")
                        .categoria("Salud")
                        .ubicacion(UbicacionDTO.builder()
                                .provincia("Salta")
                                .municipio("Salta Capital")
                                .build())
                        .fechaAcontecimiento(LocalDate.of(2024, 2, 14))
                        .etiquetas(Arrays.asList("vacunación", "salud", "gratuita", "campaña"))
                        .build()
        );
    }

}
