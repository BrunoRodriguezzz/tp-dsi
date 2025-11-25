package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.SolicitudCambiosDTO;
import ar.edu.utn.frba.dds.client.dtos.SolicitudEliminacionConHechoDTO;
import ar.edu.utn.frba.dds.client.dtos.UsuarioLogueadoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import ar.edu.utn.frba.dds.client.services.FuenteService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import ar.edu.utn.frba.dds.client.services.SolicitudesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequestMapping("/panelControl")
@Controller
public class PanelControlController {
  private final HechoService hechoService;
  private final FuenteService fuenteService;
  private final SolicitudesService solicitudesService;
  private final DinamicaService dinamicaService;

  @Value("${servicio.estatica}")
  private String fuenteEstaticaURL;

  public PanelControlController(HechoService hechoService, FuenteService fuenteService, SolicitudesService solicitudesService, DinamicaService dinamicaService) {
    this.hechoService = hechoService;
    this.fuenteService = fuenteService;
    this.solicitudesService = solicitudesService;
    this.dinamicaService = dinamicaService;
  }

  @GetMapping
  public String panelControl(Model model) {
    model.addAttribute("cantidadHechosPendientes", dinamicaService.obtenerHechosPendientes().size());
    model.addAttribute("titulo", "Panel de Control");
    model.addAttribute("cantidadHechos", hechoService.obtenerHechosAgregador().getTotalElements());
    model.addAttribute("cantModificaciones", fuenteService.obtenerFuentesNuevas().size());

    try {
      List<SolicitudEliminacionConHechoDTO> solicitudes = solicitudesService.obtenerSolicitudesEliminacion();
      model.addAttribute("cantidadSolicitudes", solicitudes.stream().filter(s -> "PENDIENTE".equalsIgnoreCase(s.getEstado())).count());
    } catch (Exception e) {
      model.addAttribute("cantidadSolicitudes", 0);
    }

    return "panelControl/index";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/hechosPendientes")
  public String panelControlHechos(Model model) {
    List<HechoDTO> hechos = this.dinamicaService.obtenerHechosPendientes();
    model.addAttribute("titulo", "Hechos Pendientes");
    model.addAttribute("hechos", hechos);
    model.addAttribute("cantidadHechos", hechos.size());
    return "panelControl/hechosPendientes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/solicitudesModificado")
  public String panelControlModificados(Model model) {
    List<SolicitudCambiosDTO> solicitudes = this.dinamicaService.obtenerSolicitudesCambios();
    model.addAttribute("titulo", "Solicitudes de Modificación");
    model.addAttribute("solicitudes", solicitudes);
    model.addAttribute("cantidadSolicitudes", solicitudes.size());
    return "panelControl/solicitudesModificado";
  }

  @GetMapping("/importarCSV")
  public String panelControlImportarCSV(Model model) {
    model.addAttribute("titulo", "Importar CSV");
    return "panelControl/importarCSV";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/importarCSV")
  public String procesarImportacionCSV(
      @RequestParam("archivo") MultipartFile archivo,
      @RequestParam(value = "nombreFuente", required = false) String nombreFuente,
      RedirectAttributes redirectAttributes) {

    try {
      // Validar que se haya subido un archivo
      if (archivo.isEmpty()) {
        redirectAttributes.addFlashAttribute("error", "Debe seleccionar un archivo CSV");
        return "redirect:/panelControl/importarCSV";
      }

      // Validar que sea un archivo CSV
      String nombreArchivo = archivo.getOriginalFilename();
      if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
        redirectAttributes.addFlashAttribute("error", "El archivo debe ser de tipo CSV");
        return "redirect:/panelControl/importarCSV";
      }

      // Usar el nombre del archivo si no se especificó un nombre de fuente
      if (nombreFuente == null || nombreFuente.trim().isEmpty()) {
        nombreFuente = nombreArchivo.replace(".csv", "").replace(".CSV", "");
      }

      // Validar estructura del CSV
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

        String primeraLinea = reader.readLine();
        if (primeraLinea == null) {
          redirectAttributes.addFlashAttribute("error", "El archivo CSV está vacío");
          return "redirect:/panelControl/importarCSV";
        }

        // Validar columnas requeridas
        String[] columnas = primeraLinea.split(",");
        boolean tieneTitulo = false, tieneDescripcion = false, tieneCategoria = false;
        boolean tieneLatitud = false, tieneLongitud = false, tieneFecha = false;

        for (String columna : columnas) {
          // Normalizar: quitar tildes, convertir a minúsculas y quitar espacios
          String columnaNormalizada = java.text.Normalizer.normalize(columna.trim(), java.text.Normalizer.Form.NFD)
              .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
              .toLowerCase();

          if (columnaNormalizada.contains("titulo")) tieneTitulo = true;
          if (columnaNormalizada.contains("descripcion")) tieneDescripcion = true;
          if (columnaNormalizada.contains("categoria")) tieneCategoria = true;
          if (columnaNormalizada.contains("latitud")) tieneLatitud = true;
          if (columnaNormalizada.contains("longitud")) tieneLongitud = true;
          if (columnaNormalizada.contains("fecha")) tieneFecha = true;
        }

        if (!tieneTitulo || !tieneDescripcion || !tieneCategoria ||
            !tieneLatitud || !tieneLongitud || !tieneFecha) {
          redirectAttributes.addFlashAttribute("error",
              "El archivo CSV debe contener las columnas: Título, Descripción, Categoría, Latitud, Longitud, Fecha del hecho");
          return "redirect:/panelControl/importarCSV";
        }
      }

      try {
        // Construir el body multipart
        org.springframework.http.client.MultipartBodyBuilder builder = new org.springframework.http.client.MultipartBodyBuilder();
        builder.part("archivo", archivo.getResource());
        builder.part("nombre", nombreFuente);

        // Usar el nuevo endpoint que acepta MultipartFile directamente
        String response = WebClient.builder()
            .baseUrl(fuenteEstaticaURL)
            .build()
            .post()
            .uri("/archivos/upload")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(builder.build())
            .retrieve()
            .onStatus(
                status -> status.value() == 409, // HTTP 409 Conflict
                clientResponse -> clientResponse.bodyToMono(String.class)
                    .map(body -> new RuntimeException(body))
            )
            .bodyToMono(String.class)
            .block();

        redirectAttributes.addFlashAttribute("mensaje",
            String.format("Archivo '%s' importado exitosamente como fuente '%s'",
                nombreArchivo, nombreFuente));
      } catch (RuntimeException e) {
        String errorMsg = e.getMessage();
        if (errorMsg != null && !errorMsg.isEmpty()) {
          redirectAttributes.addFlashAttribute("error", errorMsg);
        } else {
          redirectAttributes.addFlashAttribute("error",
              "Error al comunicarse con el servicio de fuente estática: " + e.getMessage());
        }
        return "redirect:/panelControl/importarCSV";
      } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error",
            "Error al comunicarse con el servicio de fuente estática: " + e.getMessage());
        return "redirect:/panelControl/importarCSV";
      }


    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error",
          "Error al procesar el archivo: " + e.getMessage());
    }

    return "redirect:/panelControl/importarCSV";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/solicitudes")
  public String panelControlSolicitudes(Model model, Authentication authentication) {
    try {
      List<SolicitudEliminacionConHechoDTO> solicitudes = solicitudesService.obtenerSolicitudesEliminacion();
      model.addAttribute("titulo", "Solicitudes de Eliminación");
      model.addAttribute("solicitudes", solicitudes);
      model.addAttribute("cantidadSolicitudes", solicitudes.stream().filter(s -> "PENDIENTE".equalsIgnoreCase(s.getEstado())).count());

      String username = authentication.getName();
      UsuarioLogueadoDTO usuarioLogueado = new UsuarioLogueadoDTO("Mariana", "Perez", username);
      model.addAttribute("usuarioLogueado", usuarioLogueado);

    } catch (Exception e) {
      model.addAttribute("error", "Error al cargar las solicitudes: " + e.getMessage());
      model.addAttribute("solicitudes", List.of());
      model.addAttribute("cantidadSolicitudes", 0);

      String username = authentication.getName();
      UsuarioLogueadoDTO usuarioLogueado = new UsuarioLogueadoDTO("Mariana", "Perez", username);
      model.addAttribute("usuarioLogueado", usuarioLogueado);
    }
    return "panelControl/solicitudes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/solicitudes/{id}/aceptar")
  public String aceptarSolicitud(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      String username = authentication.getName();
      solicitudesService.aceptarSolicitud(id, "Mariana", "Perez");
      redirectAttributes.addFlashAttribute("mensaje", "Solicitud aceptada correctamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al aceptar la solicitud: " + e.getMessage());
    }
    return "redirect:/panelControl/solicitudes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/solicitudes/{id}/rechazar")
  public String rechazarSolicitud(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      String username = authentication.getName();
      solicitudesService.rechazarSolicitud(id, "Mariana", "Perez");
      redirectAttributes.addFlashAttribute("mensaje", "Solicitud rechazada correctamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al rechazar la solicitud: " + e.getMessage());
    }
    return "redirect:/panelControl/solicitudes";
  }
}

