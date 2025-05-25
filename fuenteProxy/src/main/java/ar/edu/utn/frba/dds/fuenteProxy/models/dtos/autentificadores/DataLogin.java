package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores;

import lombok.Data;

@Data
public class DataLogin {
    private String access_token;
    private String token_type;
    private Usuario user;
}
