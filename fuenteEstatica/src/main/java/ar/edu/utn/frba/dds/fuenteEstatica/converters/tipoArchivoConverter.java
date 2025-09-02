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
            case archivoCSV -> "archivoCSV";
        };
    }

    @Override
    public TipoArchivoEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {return null;}

        return switch (dbData) {
            case "archivoCSV" -> TipoArchivoEnum.archivoCSV;
            default -> null;
        };
    }
}