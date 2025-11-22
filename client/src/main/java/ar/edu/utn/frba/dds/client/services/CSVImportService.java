package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.CSVImportDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class CSVImportService {
    private final RestTemplate restTemplate;

    @Value("${servicio.estatica}")
    private String fuenteEstaticaURL;

    public CSVImportDTO importarCSV(MultipartFile archivo, String nombreFuente) throws IOException {
        // Guardar el archivo temporalmente
        String uploadDir = "uploads/csv/";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String filename = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + filename);
        Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Contar líneas del CSV (aproximación de hechos)
        long cantidadHechos = Files.lines(filePath).count() - 1; // -1 por el header

        // Validar que tenga más de 10,000 entradas
        if (cantidadHechos < 10000) {
            Files.delete(filePath);
            throw new RuntimeException("El archivo debe contener más de 10,000 entradas. Contiene: " + cantidadHechos);
        }

        // Preparar request para enviar al servicio de fuente estática
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("nombre", nombreFuente != null && !nombreFuente.isEmpty() ? nombreFuente : archivo.getOriginalFilename());
        body.add("rutaArchivo", filePath.toAbsolutePath().toString());
        body.add("tipoArchivo", "CSV");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // Enviar al servicio de fuente estática para que cree la fuente
            ResponseEntity<String> response = restTemplate.postForEntity(
                    fuenteEstaticaURL + "/archivos",
                    requestEntity,
                    String.class
            );

            log.info("Respuesta del servicio de fuente estática: {}", response.getStatusCode());

            return CSVImportDTO.builder()
                    .nombreFuente(nombreFuente)
                    .rutaArchivo(filePath.toString())
                    .cantidadHechos((int) cantidadHechos)
                    .build();

        } catch (Exception e) {
            log.error("Error al enviar el CSV al servicio de fuente estática", e);
            throw new RuntimeException("Error al procesar el archivo CSV: " + e.getMessage());
        }
    }
}

