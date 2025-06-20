package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores;

import lombok.Data;

@Data
public class RequestLogin {
    private String email;
    private String password;

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
