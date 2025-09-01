package ar.edu.utn.frba.dds.fuenteEstatica.converters;

import ar.edu.utn.frba.dds.fuenteEstatica.models.enums.TipoArchivoEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class tipoArchivoConverter implements AttributeConverter<TipoArchivoEnum, String> {
    @Override
    public String convertToDatabaseColumn(TipoArchivoEnum attribute) {
        if (attribute == null) {return null;}

        return switch (attribute) {
            case TIPO1 -> "TIPO1";
            case TIPO2 -> "TIPO2";
        };
    }

    @Override
    public TipoArchivoEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {return null;}

        return switch (dbData) {
            case "TIPO1" -> TipoArchivoEnum.TIPO1;
            case "TIPO2" -> TipoArchivoEnum.TIPO2;
            default -> null;
        };
    }
}