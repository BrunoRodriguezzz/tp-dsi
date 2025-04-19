import entities.*;
import entities.services.FuenteEstatica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestCSV {
    @Test
    @DisplayName("ImportarCSV")
    public void importarCSV() {
        FuenteEstatica fuenteEstatica = new FuenteEstatica(Paths.get("public", "desastres_tecnologicos_argentina.csv").toString());
        Coleccion coleccionPrueba = new Coleccion("Colección de prueba", "Esto es una prueba");
        fuenteEstatica.cargarColeccion(coleccionPrueba);
        List<Hecho> hechosEnLaColeccion = coleccionPrueba.consultarHechos();

        Assertions.assertEquals("Chivilcoy en alerta por Emanación de gas tóxico", hechosEnLaColeccion.get(0).getTitulo());
        Assertions.assertEquals("Grave descarrilamiento de tren ocurrió en las inmediaciones de Puerto Iguazú, Misiones. El incidente provocando cortes de energía y problemas en el suministro de agua. Las autoridades locales han desplegado equipos de emergencia para atender a los afectados.", hechosEnLaColeccion.get(1).getDescripcion());
        Assertions.assertEquals("Fallo en vuelo", hechosEnLaColeccion.get(2).getCategoria().getTitulo());
        Assertions.assertEquals("-37.988737", hechosEnLaColeccion.get(3).getUbicacion().getLatitud());
        Assertions.assertEquals(LocalDate.parse("2016-11-17"), hechosEnLaColeccion.get(3).getFechaAcontecimiento());
    }

}
