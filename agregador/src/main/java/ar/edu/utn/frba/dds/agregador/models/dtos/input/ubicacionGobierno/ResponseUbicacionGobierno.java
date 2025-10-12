package ar.edu.utn.frba.dds.agregador.models.dtos.input.ubicacionGobierno;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.ProvinciaMapper;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import lombok.Data;

@Data
public class ResponseUbicacionGobierno {
    public UbicacionDTOGobierno ubicacion;

    public static Ubicacion toUbicacion(ResponseUbicacionGobierno ubicacionDTO) {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(Double.parseDouble(ubicacionDTO.ubicacion.getLat()));
        ubicacion.setLongitud(Double.parseDouble(ubicacionDTO.ubicacion.getLon()));
        ubicacion.setMunicipio(ubicacionDTO.ubicacion.getMunicipio_nombre());
        ubicacion.setProvincia(ProvinciaMapper.map(ubicacionDTO.ubicacion.getProvincia_nombre()));

        return ubicacion;
    }
}
