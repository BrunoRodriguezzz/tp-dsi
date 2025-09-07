package ar.edu.utn.frba.dds.fuenteProxy.converters;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.APICatedra;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.InstanciaMetaMapa;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoFuenteInterfazConverter implements AttributeConverter<TipoFuente, String> {
    @Override
    public String convertToDatabaseColumn(TipoFuente tipoFuente) {
        if(tipoFuente == null) {
            return null;
        }

        String fuente = "";

        if(tipoFuente instanceof APICatedra) {
            fuente = "ApiCatedra";
        }
        else if(tipoFuente instanceof InstanciaMetaMapa) {
            fuente = "InstanciaMetaMapa";
        }
        return fuente;
    }

    @Override
    public TipoFuente convertToEntityAttribute(String s) {
        if(s == null) {
            return null;
        }

        TipoFuente fuente;

        String rutaApiCatedra = "https://api-ddsi.disilab.ar/public";
        String rutaMetaMapa = ""; // TODO: falta que le llegue la url de la instancia de metamapa

        fuente = switch (s) {
            case "ApiCatedra" -> new APICatedra(rutaApiCatedra);
            case "InstanciaMetaMapa" -> new InstanciaMetaMapa(rutaMetaMapa);
            default -> null;
        };

        return fuente;
    }
}
