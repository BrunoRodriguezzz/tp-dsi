package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;

import java.util.List;

public interface IColeccionService {
    List<OutputColeccionDTO> getAll();
    List<OutputHecho> getHechosByColeccion(Long id);
    void guardarColeccion(InputColeccionDTO coleccionDTO);
}
