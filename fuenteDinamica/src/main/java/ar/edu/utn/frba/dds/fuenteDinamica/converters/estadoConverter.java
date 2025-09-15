package ar.edu.utn.frba.dds.fuenteDinamica.converters;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class estadoConverter implements AttributeConverter<EstadoHecho,String> {

    @Override
    public String convertToDatabaseColumn(EstadoHecho estado) {
        if (estado == null) return null;

        return switch (estado) {
            case PENDIENTE_DE_REVISION -> "PENDIENTE_DE_REVISION";
            case ACEPTADA -> "ACEPTADA";
            case ACEPTADA_CON_SUGERENCIA -> "ACEPTADA_CON_SUGERENCIA";
            case RECHAZADA -> "RECHAZADA";
        };
    }

    @Override
    public EstadoHecho convertToEntityAttribute(String estado) {
        if (estado == null) return null;

        return switch (estado) {
            case "PENDIENTE_DE_REVISION" -> EstadoHecho.PENDIENTE_DE_REVISION;
            case "ACEPTADA" -> EstadoHecho.ACEPTADA;
            case "ACEPTADA_CON_SUGERENCIA" -> EstadoHecho.ACEPTADA_CON_SUGERENCIA;
            case "RECHAZADA" -> EstadoHecho.RECHAZADA;
            default -> null;
        };
    }
}
