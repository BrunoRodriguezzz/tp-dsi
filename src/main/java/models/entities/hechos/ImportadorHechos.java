package models.entities.hechos;

import java.util.List;

public class ImportadorHechos {
    public void cargarColeccion(Coleccion coleccion, List<Hecho> hechos) {
        hechos.forEach(hecho -> coleccion.agregarHecho(hecho));
    }
}
