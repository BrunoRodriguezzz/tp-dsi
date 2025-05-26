package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor // <-- importante
public class FiltroProxy {
    private Long fuenteId;
    private Long idHecho;
    private String fecha; // primero como String

    public LocalDateTime getFechaParseada() {
        if (fecha != null && !fecha.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(fecha, formatter);
        }
        return null;
    }

    public Boolean cumple(HechoProxy hecho) {
        LocalDateTime fechaFiltro = getFechaParseada();
        if (fechaFiltro == null) {
            return true;
        }
        return hecho.getFechaModificacion().isAfter(fechaFiltro);
    }
}

