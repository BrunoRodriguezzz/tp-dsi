package ar.edu.utn.frba.dds.agregador.converters;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class consensoConverter implements AttributeConverter<Consenso, String> {
    @Override
    public String convertToDatabaseColumn(Consenso consenso) {
        if (consenso == null) return null;

        return switch (consenso) {
            case ABSOLUTO -> "ABSOLUTO";
            case MULTMENCIONES -> "MULTMENCIONES";
            case MAYSIMPLE -> "MAYSIMPLE";
        };
    }

    @Override
    public Consenso convertToEntityAttribute(String consenso) {
        if (consenso == null) return null;

        return switch (consenso) {
            case "ABSOLUTO" -> Consenso.ABSOLUTO;
            case "MULTMENCIONES" -> Consenso.MULTMENCIONES;
            case "MAYSIMPLE" -> Consenso.MAYSIMPLE;
            default -> null;
        };
    }
}
