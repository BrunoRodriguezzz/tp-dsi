package ar.edu.utn.frba.dds.servicioEstadisticas.clients;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfigFeign {
    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new ErrorDecoderFeign();
    }
}
