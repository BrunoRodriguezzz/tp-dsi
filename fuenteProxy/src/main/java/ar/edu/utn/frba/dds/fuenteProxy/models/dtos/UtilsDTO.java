package ar.edu.utn.frba.dds.fuenteProxy.models.dtos;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;

public class UtilsDTO {
    public static OutputHecho hechoToDtoOutput(HechoProxy hecho) {
        OutputHecho dto = new OutputHecho();
        dto.setId_hecho(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setUbicacion(hecho.getUbicacion());

        if (hecho.getFechaHecho() != null)
            dto.setFecha_hecho(hecho.getFechaHecho().toString());
        if (hecho.getFechaCreacion() != null)
            dto.setCreated_at(hecho.getFechaCreacion().toString());
        if (hecho.getFechaModificacion() != null)
            dto.setUpdated_at(hecho.getFechaModificacion().toString());
        return dto;
    }

    public static HechoProxy toHechoProxy(InputHecho input) {
        HechoProxy hecho = new HechoProxy(input.getId_hecho(), input.getTitulo());
        hecho.setDescripcion(input.getDescripcion());
        hecho.setCategoria(input.getCategoria());
        hecho.establecerUbicacion(input.getLatitud(), input.getLongitud());
        hecho.setFechaHecho(input.getFecha_hecho());
        hecho.setFechaModificacion(input.getUpdated_at());
        hecho.setFechaCreacion(input.getCreated_at());
        hecho.setIdFuente(input.getId_fuente());
        hecho.setEliminado(false);

        return hecho;
    }
}
