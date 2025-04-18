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
        hechosEnLaColeccion.forEach(hecho -> System.out.println(hecho.getTitulo())); //TODO ver una forma correcta de validación
    }

}
