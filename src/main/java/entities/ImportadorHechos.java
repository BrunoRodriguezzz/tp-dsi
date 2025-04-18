package entities;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class ImportadorHechos {
    public Integer cargarColeccion(Coleccion coleccion, List<Hecho> hechos) {
        hechos.forEach(hecho -> coleccion.agregarHecho(hecho));
        return 0;
    }
}
