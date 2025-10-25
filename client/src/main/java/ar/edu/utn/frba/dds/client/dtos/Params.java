package ar.edu.utn.frba.dds.client.dtos;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Params {
    private LocalDateTime fechaAcontecimientoInicio;
    private LocalDateTime fechaAcontecimientoFin;
    private String categoria;
    private Double lat;
    private Double lng;
    private String fuente;
    private Long page;
    private Long size;

    public Params(LocalDate fechaAcontecimientoInicio, LocalDate fechaAcontecimientoFin, String categoria, Double lat, Double lng, String fuente, Long page, Long size) {
        if (fechaAcontecimientoInicio != null)
            this.fechaAcontecimientoInicio = fechaAcontecimientoInicio.atStartOfDay();
        else
            this.fechaAcontecimientoInicio = null;
        if (fechaAcontecimientoFin != null)
            this.fechaAcontecimientoFin = fechaAcontecimientoFin.atStartOfDay();
        else
            this.fechaAcontecimientoFin = null;
        this.categoria = categoria;
        this.lat = lat;
        this.lng = lng;
        this.fuente = fuente;
        this.page = page;
        this.size = size;
    }
}
