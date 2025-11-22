package ar.edu.utn.frba.dds.fuenteProxy.clients;

import feign.Response;
import feign.codec.ErrorDecoder;

public class ErrorDecoderFeign implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new RuntimeException("Bad Request al llamar al servicio Agregador");
            case 404 -> new RuntimeException("Recurso no encontrado en el servicio Agregador");
            case 409 -> new RuntimeException("Conflicto al llamar al servicio Agregador");
            default -> new RuntimeException("Error desconocido al llamar al servicio Agregador");
        };
    }
}
