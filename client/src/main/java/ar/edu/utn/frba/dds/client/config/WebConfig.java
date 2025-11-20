package ar.edu.utn.frba.dds.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    // Leer desde application.properties (ej: ruta.archivos=./client/public/media-uploads/)
    @Value("${ruta.archivos:./public/media-uploads/}")
    private String rutaArchivos;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Resolver la ruta a absoluta para evitar problemas con el working directory
        Path p = Paths.get(rutaArchivos);
        Path abs = p.toAbsolutePath().normalize();
        String location = abs.toString();
        // Asegurar que la ruta termina con slash y usar forward slashes para URL
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        location = location.replace('\\', '/');
        String resourceLocation = location.startsWith("file:") ? location : ("file:" + location);

        logger.info("Exponiendo recursos estáticos '/media/**' desde: {}", resourceLocation);

        registry.addResourceHandler("/media/**")
                .addResourceLocations(resourceLocation);
    }
}