package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores;

import lombok.Data;

@Data
public class Usuario {
    private Long id;
    private String email;
    private String nombre;

}

