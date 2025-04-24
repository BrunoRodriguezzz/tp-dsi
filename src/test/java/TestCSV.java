import models.entities.hechos.Hecho;
import models.entities.hechos.FuenteEstatica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.util.List;

public class TestCSV {
    @Test
    @DisplayName("ImportarCSV")
    public void importarCSV() {
        FuenteEstatica fuenteEstatica = new FuenteEstatica(Paths.get("public", "desastres_tecnologicos_argentina.csv").toString());

        List<Hecho> hechos = fuenteEstatica.importarHechos();

        Assertions.assertEquals("Chivilcoy en alerta por Emanación de gas tóxico", hechos.get(0).getTitulo());
        Assertions.assertEquals("Grave descarrilamiento de tren ocurrió en las inmediaciones de Puerto Iguazú, Misiones. El incidente provocando cortes de energía y problemas en el suministro de agua. Las autoridades locales han desplegado equipos de emergencia para atender a los afectados.", hechos.get(1).getDescripcion());
        Assertions.assertEquals("Fallo en vuelo", hechos.get(2).getCategoria().getTitulo());
        Assertions.assertEquals("-37.988737", hechos.get(3).getUbicacion().getLatitud());
        Assertions.assertEquals(LocalDate.parse("2016-11-17"), hechos.get(4).getFechaAcontecimiento());
    }
}
