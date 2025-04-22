package models.entities.valueObjectsHecho;

public class ContenidoMultimedia {
    private String nombre;
    private String ruta;
    private Formato formato;

    public ContenidoMultimedia(String nombre, String ruta, Formato formato) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.formato = formato;
    }
}
