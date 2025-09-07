package ar.edu.utn.frba.dds.agregador.converters;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoFuenteConverter implements AttributeConverter<TipoFuente, String> {
    @Override
    public String convertToDatabaseColumn(TipoFuente attribute) {
        if (attribute == null) {return null;}

        return switch (attribute) {
            case ESTATICA -> "ESTATICA";
            case DINAMICA -> "DINAMICA";
            case PROXY -> "PROXY";
        };
    }

    @Override
    public TipoFuente convertToEntityAttribute(String dbData) {
        if (dbData == null) {return null;}

        return switch (dbData) {
            case "ESTATICA" -> TipoFuente.ESTATICA;
            case "DINAMICA" -> TipoFuente.DINAMICA;
            case "PROXY" -> TipoFuente.PROXY;
            default -> null;
        };
    }
}
