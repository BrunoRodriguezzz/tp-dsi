package entities;

import lombok.Setter;

import java.time.LocalDate;

@Setter
public class Criterio {
    private String requisitoTitulo;
    private String requisitoDescripcion;
    private Origen origen;
    private Categoria categoria;
    private Boolean tieneContenidoMultimedia;
    private RangoFechas fechaAcontecimientoEntre;
    private RangoFechas fechaDeCargaEntre;

    public Criterio() {
        this.requisitoTitulo = null;
        this.requisitoDescripcion = null;
        this.origen = null;
        this.categoria = null;
        this.tieneContenidoMultimedia = false;
        this.fechaAcontecimientoEntre = null;
        this.fechaDeCargaEntre = null;
    }


    public Boolean cumpleCriterio(Hecho hecho) {
        return (this.cumpleTitulo(hecho.getTitulo()) &&
                this.cumpleDescripcion(hecho.getDescripcion()) &&
                this.cumpleOrigen(hecho.getOrigen()) &&
                this.cumpleCategoria(hecho.getCategoria()) &&
                this.cumpleFechaAcontecimientoEntre(hecho.getFechaAcontecimiento()) &&
                this.cumpleFechaDeCargaEntre(hecho.getFechaCarga())
        );
    }

    //TODO: Metodos todos iguales
    private Boolean cumpleTitulo(String titulo) {
        if(requisitoTitulo == null || requisitoTitulo.isEmpty() ) {
            return true;
        }
        return titulo.contains(this.requisitoTitulo);
    }

    private Boolean cumpleDescripcion(String descripcion) {
        if(requisitoDescripcion == null || requisitoDescripcion.isEmpty()) {
            return true;
        }
        return descripcion.contains(this.requisitoDescripcion);
    }

    private Boolean cumpleOrigen(Origen origen) {
        if(this.origen == null){
            return true;
        }
        return this.origen.equals(origen);
    }

    private Boolean cumpleCategoria(Categoria categoria) {
        if(this.categoria == null){
            return true;
        }
        return categoria.coincide(this.categoria);
    }

    private Boolean cumpleFechaAcontecimientoEntre(LocalDate fecha) {
        if(this.fechaAcontecimientoEntre == null){
            return true;
        }
        return fechaAcontecimientoEntre.coincide(fecha);
    }

    private Boolean cumpleFechaDeCargaEntre(LocalDate fecha) {
        if(this.fechaDeCargaEntre == null){
            return true;
        }
        return fechaDeCargaEntre.coincide(fecha);
    }
}
