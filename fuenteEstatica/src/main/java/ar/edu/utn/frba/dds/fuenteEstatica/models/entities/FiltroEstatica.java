package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.ValidationError;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class FiltroEstatica {
    private Long idHecho;
    private LocalDateTime fecha; // primero como String
    private Boolean nuevos;

    public void validate() {
        if(idHecho != null) {
            if(idHecho <= 0) {
                throw new ValidationError("fuenteId tiene que ser mayor a 0, ID ingresado: " + idHecho);
            }
        }

        if(this.fecha != null) {
            if(this.fecha.isAfter(LocalDateTime.now())) {
                throw new ValidationError("Fecha invalida: " + this.fecha);
            }
        }
    }

    public boolean nuevos() {
        return this.nuevos != null && this.nuevos;
    }

    public boolean esVacio() {
        return this.idHecho == null && this.fecha == null && !nuevos();
    }
}
