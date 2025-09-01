package ar.edu.utn.frba.dds.agregador.converters;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class origenConverter implements AttributeConverter<Origen, String> {
    @Override
    public String convertToDatabaseColumn(Origen origen) {
        if(origen == null) return null;

        return switch (origen) {
            case PROXY -> "PROXY";
            case MANUAL -> "MANUAL";
            case DATASET -> "DATASET";
            case CONTRIBUYENTE -> "CONTRIBUYENTE";
        };
    }

    @Override
    public Origen convertToEntityAttribute(String origen) {
        if(origen == null) return null;

        return switch (origen) {
            case "PROXY" -> Origen.PROXY;
            case "MANUAL" -> Origen.MANUAL;
            case "DATASET" -> Origen.DATASET;
            case "CONTRIBUYENTE" -> Origen.CONTRIBUYENTE;
            default -> null;
        };
    }
}
