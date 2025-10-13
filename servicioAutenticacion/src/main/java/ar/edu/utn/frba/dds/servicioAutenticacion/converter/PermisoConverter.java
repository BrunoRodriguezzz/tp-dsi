package ar.edu.utn.frba.dds.servicioAutenticacion.converter;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Permiso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PermisoConverter implements AttributeConverter<Permiso, String> {
    @Override
    public String convertToDatabaseColumn(Permiso permiso) {
        if (permiso == null) return null;
        return permiso.name();
    }

    @Override
    public Permiso convertToEntityAttribute(String permiso) {
        if (permiso == null) return null;
        return Permiso.valueOf(permiso);
    }
}
