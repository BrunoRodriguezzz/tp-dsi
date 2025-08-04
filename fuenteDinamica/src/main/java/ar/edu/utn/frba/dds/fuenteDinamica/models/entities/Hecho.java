package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class Hecho {
    private Long          id;
    private Contribuyente contribuyente;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private Ubicacion     ubicacion;
    private LocalDate     fechaAcontecimiento;
    private List<String>  etiquetas;
    private LocalDateTime fechaGuardado;
    private LocalDateTime fechaModificacion;
    private EstadoHecho   estadoHecho;
    private String        sugerenciaDeCambio;
    private Boolean       enviado;
    private Boolean       estaEliminado;
    private String        origen;
    private String        fuente;
}