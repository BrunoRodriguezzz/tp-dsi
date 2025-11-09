package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestLogin {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
