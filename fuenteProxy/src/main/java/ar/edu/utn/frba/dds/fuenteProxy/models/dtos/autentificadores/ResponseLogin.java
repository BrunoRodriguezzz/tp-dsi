package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores;

import lombok.Data;

@Data
public class ResponseLogin {
    private Boolean error;
    private String message;
    private DataLogin data;


    public Boolean esError() {
        return error;
    }

}