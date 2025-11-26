package ar.edu.utn.frba.dds.fuenteDinamica.clients;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorDecoderFeign implements ErrorDecoder {

  private static final Logger logger = LoggerFactory.getLogger(ErrorDecoderFeign.class);

  @Override
  public Exception decode(String methodKey, Response response) {

    // Capturar información detallada del error
    int status = response.status();
    String reason = response.reason();
    String url = response.request().url();

    // Log detallado para debugging
    logger.error("=== FEIGN CLIENT ERROR DEBUG ===");
    logger.error("Method: {}", methodKey);
    logger.error("Status Code: {}", status);
    logger.error("Reason: {}", reason);
    logger.error("URL: {}", url);

    // Intentar leer el cuerpo de la respuesta para más detalles
    String responseBody = "No body available";
    try {
      if (response.body() != null) {
        responseBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        logger.error("Response Body: {}", responseBody);
      }
    } catch (IOException e) {
      logger.error("Error reading response body: {}", e.getMessage());
    }
    logger.error("=====================================");

    // Manejo específico por código de estado
    return switch (status) {
      case 400 -> new RuntimeException(
          String.format("Bad Request al llamar al servicio Agregador. URL: %s, Reason: %s, Body: %s",
              url, reason, responseBody));

      case 401 -> new RuntimeException(
          String.format("Unauthorized al llamar al servicio Agregador. URL: %s, Reason: %s",
              url, reason));

      case 403 -> new RuntimeException(
          String.format("Forbidden al llamar al servicio Agregador. URL: %s, Reason: %s",
              url, reason));

      case 404 -> new RuntimeException(
          String.format("Recurso no encontrado en el servicio Agregador. URL: %s, Reason: %s",
              url, reason));

      case 409 -> new RuntimeException(
          String.format("Conflicto al llamar al servicio Agregador. URL: %s, Reason: %s, Body: %s",
              url, reason, responseBody));

      case 500 -> new RuntimeException(
          String.format("Internal Server Error en el servicio Agregador. URL: %s, Reason: %s, Body: %s",
              url, reason, responseBody));

      case 502 -> new RuntimeException(
          String.format("Bad Gateway al llamar al servicio Agregador. URL: %s, Reason: %s",
              url, reason));

      case 503 -> new RuntimeException(
          String.format("Service Unavailable en el servicio Agregador. URL: %s, Reason: %s",
              url, reason));

      default -> new RuntimeException(
          String.format("Error HTTP %d al llamar al servicio Agregador. URL: %s, Reason: %s, Body: %s",
              status, url, reason, responseBody));
    };
  }
}
