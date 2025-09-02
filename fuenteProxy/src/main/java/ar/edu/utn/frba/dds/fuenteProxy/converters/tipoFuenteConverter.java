package ar.edu.utn.frba.dds.fuenteProxy.converters;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.enums.TipoFuenteEnum;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.APICatedra;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.InstanciaMetaMapa;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class tipoFuenteConverter implements AttributeConverter<TipoFuenteEnum, String> {
    @Override
    public String convertToDatabaseColumn(TipoFuenteEnum attribute) {
        if (attribute == null) {return null;}

        return switch (attribute) {
            case APICATEDRA -> "APICATEDRA";
            case INSTANCIAMETAMAPA -> "INSTANCIAMETAMAPA";
        };
    }

    @Override
    public TipoFuenteEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {return null;}

        return switch (dbData) {
            case "APICATEDRA" -> TipoFuenteEnum.APICATEDRA;
            case "INSTANCIAMETAMAPA" -> TipoFuenteEnum.INSTANCIAMETAMAPA;
            default -> null;
        };
    }
}
