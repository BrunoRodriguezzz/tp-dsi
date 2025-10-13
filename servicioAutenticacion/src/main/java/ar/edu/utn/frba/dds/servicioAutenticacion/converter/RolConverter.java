package ar.edu.utn.frba.dds.servicioAutenticacion.converter;


import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Rol;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolConverter implements AttributeConverter<Rol, String> {
    @Override
    public String convertToDatabaseColumn(Rol rol) {
        if (rol == null) return null;
        return rol.name();
    }

    @Override
    public Rol convertToEntityAttribute(String rol) {
        if (rol == null) return null;
        return Rol.valueOf(rol);
    }
}
