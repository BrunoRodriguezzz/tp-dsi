package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores;

import lombok.Data;

@Data
public class RequestLogin {
    private String usernname;
    private String password;

    public RequestLogin(String usernname, String password) {
        this.usernname = usernname;
        this.password = password;
    }
}
