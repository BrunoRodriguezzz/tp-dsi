package ar.edu.utn.frba.dds.fuenteProxy.converters;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.APICatedra;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.InstanciaMetaMapa;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class tipoFuenteConverter implements AttributeConverter<TipoFuente, String> {
    @Override
    public String convertToDatabaseColumn(TipoFuente attribute) {
        if (attribute instanceof APICatedra) {
            return "APICatedra";
        } else if (attribute instanceof InstanciaMetaMapa) {
            return "InstanciaMetaMapa";
        }
        return null;
    }

    @Override
    public TipoFuente convertToEntityAttribute(String dbData) {

    }
}
