package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl.ArchivoCSV;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IArchivoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/archivos")
@RequiredArgsConstructor
public class ArchivoController {
    private final IArchivoService archivoService;
    private final IArchivoRepository archivoRepository;

    @PostMapping
    public ResponseEntity<String> crearFuenteDesdeCSV(
            @RequestParam("nombre") String nombre,
            @RequestParam("rutaArchivo") String rutaArchivo,
            @RequestParam("tipoArchivo") String tipoArchivo
    ) {
        try {
            log.info("Creando nueva fuente estática: {} desde archivo: {}", nombre, rutaArchivo);

            Archivo archivo = new Archivo();
            archivo.setNombre(nombre);
            archivo.setRutaArchivo(rutaArchivo);
            archivo.setUltimaConsulta(LocalDateTime.now());

            if ("CSV".equalsIgnoreCase(tipoArchivo)) {
                archivo.setTipoArchivo(new ArchivoCSV());
            } else {
                return ResponseEntity.badRequest().body("Tipo de archivo no soportado: " + tipoArchivo);
            }

            archivoService.guardarArchivo(archivo);

            log.info("Fuente estática creada exitosamente: {}", nombre);
            return ResponseEntity.status(HttpStatus.CREATED).body("Fuente estática creada exitosamente");

        } catch (Exception e) {
            log.error("Error al crear fuente estática desde CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> crearFuenteDesdeArchivoSubido(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "nombre", required = false) String nombre
    ) {
        try {
            // Validar que se haya subido un archivo
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body("Debe seleccionar un archivo CSV");
            }

            // Validar que sea un archivo CSV
            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("El archivo debe ser de tipo CSV");
            }

            // Usar el nombre del archivo si no se especificó un nombre de fuente
            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = nombreArchivo.replace(".csv", "").replace(".CSV", "");
            }

            // Verificar si la fuente ya existe en la base de datos
            List<Archivo> archivosExistentes = archivoRepository.findByNombre(nombre);
            if (!archivosExistentes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("La fuente '" + nombre + "' ya fue importada. No se puede importar dos veces la misma fuente.");
            }

            // Determinar la ruta de destino
            Path targetPath = determinarRutaDestino(nombreArchivo);

            // Verificar si el archivo ya existe físicamente
            boolean archivoExisteFisicamente = Files.exists(targetPath);

            if (!archivoExisteFisicamente) {
                // Solo copiar si el archivo NO existe
                try {
                    Files.copy(archivo.getInputStream(), targetPath);
                } catch (IOException e) {
                    throw new IOException("No se pudo guardar el archivo: " + e.getMessage(), e);
                }
            }

            // Crear la entidad Archivo con ruta relativa
            String rutaRelativa = "domain/src/test/resources/" + nombreArchivo;

            Archivo archivoEntity = new Archivo();
            archivoEntity.setNombre(nombre);
            archivoEntity.setRutaArchivo(rutaRelativa);
            archivoEntity.setUltimaConsulta(LocalDateTime.now());
            archivoEntity.setTipoArchivo(new ArchivoCSV());

            try {
                log.info("✅ Archivo guardado físicamente en: {}", targetPath);
                archivoService.guardarArchivoSync(archivoEntity);
                log.info("🎉 Fuente estática creada exitosamente: {}", nombre);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Fuente estática creada exitosamente desde archivo: " + nombreArchivo);
            } catch (Exception e) {
                if (!archivoExisteFisicamente && Files.exists(targetPath)) {
                    try {
                        Files.delete(targetPath);
                    } catch (IOException deleteError) {
                    }
                }
                throw e;
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el archivo: " + e.getMessage());
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("NumberFormatException")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: El archivo CSV tiene un formato incorrecto. Verifica que las columnas de latitud y longitud contengan números válidos.");
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + errorMsg);
        }
    }

    private Path determinarRutaDestino(String nombreArchivo) throws IOException {
        // Obtener el directorio actual
        Path currentPath = Paths.get("").toAbsolutePath();

        Path workspacePath;
        if (currentPath.endsWith("fuenteEstatica")) {
            workspacePath = currentPath.getParent();
        } else {
            workspacePath = currentPath;
        }

        // Construir la ruta a domain/src/test/resources
        Path domainResourcesPath = workspacePath
            .resolve("domain")
            .resolve("src")
            .resolve("test")
            .resolve("resources");

        // Verificar si existe, si no, crearla
        if (!Files.exists(domainResourcesPath)) {
            Files.createDirectories(domainResourcesPath);
        }

        return domainResourcesPath.resolve(nombreArchivo);
    }
}


