package ar.edu.utn.frba.dds.fuenteProxy.models.dtos;

import lombok.Getter;

@Getter
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String fechaHecho;
    private String createdAt;
    private String updatedAt;
    private Long idFuente;
    private Long idHecho;
}
