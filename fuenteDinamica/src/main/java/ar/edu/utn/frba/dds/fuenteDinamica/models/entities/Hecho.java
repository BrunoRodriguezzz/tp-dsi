package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import java.time.LocalDate;

public class Hecho {

    private Long          id;
    private Contribuyente contribuyente;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private String        urlMultimedia;
    private String        ubicacion;
    private LocalDate     fechaAcontecimiento;
    private LocalDate     fechaCarga;

    public Boolean esContribuyente(){
        if(contribuyente != null){
            //TODO: Falta comprobar que no haya pasado una semana, porque despues no podra editar
            return true;
        }else{
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
}