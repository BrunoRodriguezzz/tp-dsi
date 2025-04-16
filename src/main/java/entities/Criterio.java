package entities;

public class Criterio {
    private String requisitoTitulo;
    private String requisitoDescripcion;
    private Origen origen;
    private Categoria categoria;
    private Boolean tieneContenidoMultimedia;
    private RangoFechas fechaAcontecimientoEntre;
    private RangoFechas fechaDeCargaEntre;

    public Boolean cumpleCriterio(Hecho hecho) {
        return (this.tituloContiene(hecho.getTitulo()) &&
                this.descripcionContiene(hecho.getDescripcion()) &&
                this.cumpleOrigen(hecho.getOrigen())
//                this.categoria.coincide(hecho.getCategoria()) && TODO: Try Catch
//                this.fechaAcontecimientoEntre.coincide(hecho.getfechaAcontecimiento()) && TODO: Try Catch
//                this.fechaAcontecimientoEntre.coincide(hecho.getfechaCarga()) && TODO: Try Catch
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
