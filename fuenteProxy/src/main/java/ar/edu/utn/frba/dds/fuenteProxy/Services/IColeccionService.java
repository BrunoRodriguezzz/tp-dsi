package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;

import java.util.List;

public interface IColeccionService {
    List<InputColeccionDTO> getAll();
    List<OutputHecho> getHechosByColeccion(String identificador);
    void guardarHecho(Coleccion coleccion);
}
