package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.ValidationError;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class FiltroEstatica {
    private Long archivoId;
    private Long idHecho;
    private String fecha;
    private LocalDateTime date; // primero como String

    public void validate() {
        if(archivoId != null) {
            if(archivoId <= 0) {
                throw new ValidationError("fuenteId tiene que ser mayor a 0, ID ingresado: " + archivoId);
            }
        }
        if(idHecho != null) {
            if(idHecho <= 0) {
                throw new ValidationError("fuenteId tiene que ser mayor a 0, ID ingresado: " + idHecho);
            }
        }

        this.date = this.getFechaParseada(fecha);

        if(this.date != null) {
            if(this.date.isAfter(LocalDateTime.now())) {
                throw new ValidationError("Fecha invalida: " + this.fecha);
            }
        }
    }

    public LocalDateTime getFechaParseada(String fecha) {
        if (fecha != null && !fecha.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(fecha, formatter);
        }
        return null;
    }

    public Boolean cumple(HechoEstatica hecho) {
        if (this.date == null) {
            return true;
        }
        return hecho.getFechaModificacion().isAfter(this.date) ;
    }
}
