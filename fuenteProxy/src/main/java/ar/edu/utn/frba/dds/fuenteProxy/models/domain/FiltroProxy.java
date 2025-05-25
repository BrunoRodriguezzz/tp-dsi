package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class FiltroProxy {
    private Long fuenteId;
    private Long idHecho;
    private LocalDateTime fecha;

    public FiltroProxy(Long fuenteId, Long idHecho, String fecha) { //TODO Validar fecha acorde, ID >= 0
        LocalDateTime fechaParseada = null;
        if (fecha != null && !fecha.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            fechaParseada = LocalDateTime.parse(fecha, formatter);
        }

        this.fuenteId = fuenteId;
        this.idHecho = idHecho;
        this.fecha = fechaParseada;
    }
}
