package ar.edu.utn.frba.dds.fuenteEstatica.converters;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.TipoArchivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl.ArchivoCSV;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoArchivoConverter implements AttributeConverter<TipoArchivo, String> {
    @Override
    public String convertToDatabaseColumn(TipoArchivo tipo) {
        if (tipo instanceof ArchivoCSV) return "ArchivoCSV";
        return null;
    }

    @Override
    public TipoArchivo convertToEntityAttribute(String dbData) {
        if("ArchivoCSV".equals(dbData)) return new ArchivoCSV();
        return null;
    }
}