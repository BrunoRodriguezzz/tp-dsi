package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataLogin {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("user")
    private Usuario user;
}
