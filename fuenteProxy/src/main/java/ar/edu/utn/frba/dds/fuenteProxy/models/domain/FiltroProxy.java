package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.exceptions.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class FiltroProxy {
    private Long fuenteId;
    private Long idHecho;
    private String fecha;
    private LocalDateTime date; // primero como String

    public void validate() {
        if(fuenteId != null) {
            if(fuenteId <= 0) {
                throw new ValidationError("fuenteId tiene que ser mayor a 0, ID ingresado: " + fuenteId);
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

    public Boolean cumple(HechoProxy hecho) {
        if (this.date == null) {
            return true;
        }
        return hecho.getFechaModificacion().isAfter(this.date);
    }
}

