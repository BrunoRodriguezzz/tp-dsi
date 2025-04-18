package entities;

public class Criterio {
    private String requisitoTitulo;
    private String requisitoDescripcion;
    private Origen origen;
    private Categoria categoria;
    private Boolean tieneContenidoMultimedia;
    private RangoFechas fechaAcontecimientoEntre;
    private RangoFechas fechaDeCargaEntre;

    public Criterio(String requisitoTitulo, String requisitoDescripcion, Origen origen, Categoria categoria,
                    RangoFechas fechaAcontecimientoEntre, RangoFechas fechaDeCargaEntre) {
        this.requisitoTitulo = requisitoTitulo;
        this.requisitoDescripcion = requisitoDescripcion;
        this.origen = origen;
        this.categoria = categoria;
        this.fechaAcontecimientoEntre = fechaAcontecimientoEntre;
        this.fechaDeCargaEntre = fechaDeCargaEntre;
    }

    public Boolean cumpleCriterio(Hecho hecho) {
        return (this.tituloContiene(hecho.getTitulo()) &&
                this.descripcionContiene(hecho.getDescripcion()) &&
                this.cumpleOrigen(hecho.getOrigen()) &&
                this.categoria.coincide(hecho.getCategoria()) &&
                this.fechaAcontecimientoEntre.coincide(hecho.getFechaAcontecimiento()) &&
                this.fechaAcontecimientoEntre.coincide(hecho.getFechaCarga())
        );
    }

    private Boolean tituloContiene(String titulo) {
        if(titulo == null || titulo.isEmpty()) {
            return true;
        }
        return titulo.contains(this.requisitoTitulo);
    }

    private Boolean descripcionContiene(String descripcion) {
        if(descripcion == null || descripcion.isEmpty()) {
            return true;
        }
        return descripcion.contains(this.requisitoDescripcion);
    }

    private Boolean cumpleOrigen(Origen origen) {
        return this.origen.equals(origen);
    }
}
