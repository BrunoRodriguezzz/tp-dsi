package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl.ArchivoCSV;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IArchivoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/archivos")
@RequiredArgsConstructor
public class ArchivoController {
    private final IArchivoService archivoService;

    @PostMapping
    public ResponseEntity<String> crearFuenteDesdeCSV(
            @RequestParam("nombre") String nombre,
            @RequestParam("rutaArchivo") String rutaArchivo,
            @RequestParam("tipoArchivo") String tipoArchivo
    ) {
        try {
            log.info("Creando nueva fuente estática: {} desde archivo: {}", nombre, rutaArchivo);

            // Crear el objeto Archivo
            Archivo archivo = new Archivo();
            archivo.setNombre(nombre);
            archivo.setRutaArchivo(rutaArchivo);
            archivo.setUltimaConsulta(LocalDateTime.now());

            // Determinar el tipo de archivo
            if ("CSV".equalsIgnoreCase(tipoArchivo)) {
                archivo.setTipoArchivo(new ArchivoCSV());
            } else {
                return ResponseEntity.badRequest().body("Tipo de archivo no soportado: " + tipoArchivo);
            }

            // Guardar el archivo (esto automáticamente importará los hechos)
            archivoService.guardarArchivo(archivo);

            log.info("Fuente estática creada exitosamente: {}", nombre);
            return ResponseEntity.status(HttpStatus.CREATED).body("Fuente estática creada exitosamente");

        } catch (Exception e) {
            log.error("Error al crear fuente estática desde CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        }
    }
}


