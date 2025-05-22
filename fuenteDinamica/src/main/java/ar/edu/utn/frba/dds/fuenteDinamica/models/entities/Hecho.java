package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class Hecho {

    private Long          idHecho;
    private Contribuyente contribuyente;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private String        urlMultimedia;
    private String        ubicacion;
    private LocalDate     fechaAcontecimiento;
    private List<String>  etiquetas;
    private LocalDate     fechaGuardado;
    private EstadoHecho   estadoHecho;
    private String        sugerenciaDeCambio;
    private Boolean       enviado;

}