package ar.edu.utn.frba.dds.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaProvinciaXColeccionDTO {
    private String coleccion;
    private Map<String, Long> provinciasConHechos;
}
// TODO: ponerle un OutputDTO al servicio de estadísticas para que me lo mande así