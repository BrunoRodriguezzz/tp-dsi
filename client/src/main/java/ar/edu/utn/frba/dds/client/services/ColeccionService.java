package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.CriterioDTO;
import ar.edu.utn.frba.dds.client.dtos.FiltroDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ColeccionService {
  private final WebClient webClient;
  private final WebApiCallerService webApiCallerService;
  private final String hechoServiceUrl;
  private final MockService mockService;

  public ColeccionService(
      WebApiCallerService webApiCallerService,
      @Value("${hechos.service.url}") String hechoServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.hechoServiceUrl = hechoServiceUrl;
    this.mockService = new MockService();
  }

  public List<HechoDTO> obtenerHechosDestacados() {
//        List<HechoDTO> response = this.webApiCallerService.getList(this.hechoServiceUrl, HechoDTO.class);
//        return response != null ? response : List.of();
    return this.mockService.obtenerHechosMockeados();
  }

  public List<HechoDTO> obtenerHechos() {
    return mockService.obtenerHechosMockeados();
  }

  public HechoDTO obtenerHechoPorId(Long id) {
    HechoDTO hecho = mockService.obtenerHechoPorId(id);
    return hecho;
  }

  public List<ColeccionOutputDTO> obtenerColecciones() {
        FiltroDTO filtro1 = new FiltroDTO();
        filtro1.setNombre("Año");
        filtro1.setDetalle("2024");
        FiltroDTO filtro2 = new FiltroDTO();
        filtro2.setNombre("Tipo");
        filtro2.setDetalle("Protesta social");
        ArrayList<FiltroDTO> filtros1 = new ArrayList<>();
        filtros1.add(filtro1);
        filtros1.add(filtro2);
        CriterioDTO criterio1 = new CriterioDTO();
        criterio1.setFiltros(filtros1);

        FiltroDTO filtro3 = new FiltroDTO();
        filtro3.setNombre("Tema");
        filtro3.setDetalle("Medio ambiente");
        ArrayList<FiltroDTO> filtros2 = new ArrayList<>();
        filtros2.add(filtro3);
        CriterioDTO criterio2 = new CriterioDTO();
        criterio2.setFiltros(filtros2);

        // Colecciones adicionales mockeadas
        List<ColeccionOutputDTO> colecciones = new ArrayList<>(Arrays.asList(
            new ColeccionOutputDTO(
                1L,
                "Protestas Sociales 2024",
                "Recopilación de manifestaciones y protestas sociales registradas durante el año 2024, incluyendo marchas por derechos laborales, estudiantes y civiles.",
                15L,
                criterio1,
                Arrays.asList("Página12", "Clarín", "La Nación"),
                Arrays.asList("Multiples Menciones", "Mayoría Simple", "Mayoría Absoluta")
            ),
            new ColeccionOutputDTO(
                2L,
                "Movimientos Ambientales",
                "Hechos relacionados con protestas y acciones por el medio ambiente.",
                8L,
                criterio2,
                Arrays.asList("Greenpeace", "Infobae"),
                Arrays.asList("Multiples Menciones", "Mayoría Simple")
            )
        ));
        // Agrego colecciones extra para probar el carousel
        for (long i = 3; i <= 8; i++) {
            colecciones.add(new ColeccionOutputDTO(
                i,
                "Colección Mock " + i,
                "Descripción de la colección mock número " + i,
                5L + i,
                criterio1,
                Arrays.asList("Fuente " + i),
                Arrays.asList("Algoritmo X", "Algoritmo Y")
            ));
        }
        return colecciones;
    }

  public ColeccionOutputDTO obtenerColeccionPorId(Long id) {
    return this.obtenerColecciones().stream()
        .filter(c -> c.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public List<HechoDTO> obtenerHechosPorColeccionId(Long coleccionId) {
    // En este mock, simplemente devolvemos todos los hechos, pero podrías filtrar por coleccionId si lo deseas
    return mockService.obtenerHechosMockeados();
  }
}
