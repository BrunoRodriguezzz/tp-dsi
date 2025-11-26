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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RequestMapping("/panelControl")
@Controller
public class PanelControlController {
  private final HechoService hechoService;
  private final FuenteService fuenteService;
  private final SolicitudesService solicitudesService;
  private final DinamicaService dinamicaService;

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

      // Validar estructura del archivo CSV
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

        String primeraLinea = reader.readLine();
        if (primeraLinea == null || primeraLinea.trim().isEmpty()) {
          redirectAttributes.addFlashAttribute("error", "El archivo CSV está vacío");
          return "redirect:/panelControl/importarCSV";
        }

        // Validar que tenga al menos 6 columnas
        String[] columnas = primeraLinea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Split respetando comillas
        if (columnas.length < 6) {
          redirectAttributes.addFlashAttribute("error",
              "El archivo CSV debe tener al menos 6 columnas: Título, Descripción, Categoría, Latitud, Longitud, Fecha");
          return "redirect:/panelControl/importarCSV";
        }

        // Detectar si la primera línea es encabezado intentando parsear latitud/longitud
        boolean primeraLineaEsEncabezado = false;
        try {
          Double.parseDouble(columnas[3].trim().replace("\"", ""));
          Double.parseDouble(columnas[4].trim().replace("\"", ""));
        } catch (NumberFormatException e) {
          primeraLineaEsEncabezado = true;
        }

        // Obtener la línea de datos a validar
        String[] datosAValidar;
        if (primeraLineaEsEncabezado) {
          // Si hay encabezado, leer la siguiente línea
          String segundaLinea = reader.readLine();
          if (segundaLinea == null || segundaLinea.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                "El archivo CSV solo contiene encabezados, debe incluir al menos un hecho");
            return "redirect:/panelControl/importarCSV";
          }
          datosAValidar = segundaLinea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        } else {
          // Si no hay encabezado, usar la primera línea
          datosAValidar = columnas;
        }

        // Validar que los datos tengan 6 columnas
        if (datosAValidar.length < 6) {
          redirectAttributes.addFlashAttribute("error",
              "Los datos del CSV deben tener 6 columnas por fila");
          return "redirect:/panelControl/importarCSV";
        }

        // Validar que latitud y longitud sean números
        try {
          Double.parseDouble(datosAValidar[3].trim().replace("\"", ""));
          Double.parseDouble(datosAValidar[4].trim().replace("\"", ""));
        } catch (NumberFormatException e) {
          redirectAttributes.addFlashAttribute("error",
              "Las columnas de Latitud y Longitud deben contener números válidos");
          return "redirect:/panelControl/importarCSV";
        }

        // Validar formato de fecha (dd/MM/yyyy)
        String fecha = datosAValidar[5].trim().replace("\"", "");
        if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
          redirectAttributes.addFlashAttribute("error",
              "La columna de Fecha debe tener el formato dd/MM/yyyy (ejemplo: 15/03/2019)");
          return "redirect:/panelControl/importarCSV";
        }
      } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error",
            "Error al validar el archivo CSV: " + e.getMessage());
        return "redirect:/panelControl/importarCSV";
      }

      try {
        String response = fuenteService.importarCSV(archivo, nombreFuente);

        redirectAttributes.addFlashAttribute("mensaje",
            String.format("Archivo '%s' importado exitosamente como fuente '%s'",
                nombreArchivo, nombreFuente));
      } catch (RuntimeException e) {
        String errorMsg = e.getMessage();
        if (errorMsg != null && !errorMsg.isEmpty()) {
          // Verificar si es un error de conflicto (fuente duplicada)
          if (errorMsg.contains("409") || errorMsg.contains("Conflict") || errorMsg.contains("ya fue importada")) {
            redirectAttributes.addFlashAttribute("error",
                "La fuente '" + nombreFuente + "' ya fue importada. No se puede importar dos veces la misma fuente.");
          } else {
            redirectAttributes.addFlashAttribute("error", errorMsg);
          }
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
      UsuarioLogueadoDTO usuarioLogueado = new UsuarioLogueadoDTO("Valentin", "Bravo", username);
      model.addAttribute("usuarioLogueado", usuarioLogueado);

    } catch (Exception e) {
      model.addAttribute("error", "Error al cargar las solicitudes: " + e.getMessage());
      model.addAttribute("solicitudes", List.of());
      model.addAttribute("cantidadSolicitudes", 0);

      String username = authentication.getName();
      UsuarioLogueadoDTO usuarioLogueado = new UsuarioLogueadoDTO("Valentin", "Bravo", username);
      model.addAttribute("usuarioLogueado", usuarioLogueado);
    }
    return "panelControl/solicitudes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/solicitudes/{id}/aceptar")
  public String aceptarSolicitud(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      String username = authentication.getName();
      solicitudesService.aceptarSolicitud(id, "Valentin", "Bravo");
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
      solicitudesService.rechazarSolicitud(id, "Valentin", "Bravo");
      redirectAttributes.addFlashAttribute("mensaje", "Solicitud rechazada correctamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al rechazar la solicitud: " + e.getMessage());
    }
    return "redirect:/panelControl/solicitudes";
  }
}

