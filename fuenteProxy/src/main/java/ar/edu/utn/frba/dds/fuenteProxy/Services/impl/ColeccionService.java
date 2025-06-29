package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;
import ar.edu.utn.frba.dds.fuenteProxy.Services.IColeccionService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.exceptions.NotFoundError;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColeccionService implements IColeccionService {

    @Autowired
    private IColeccionRepository coleccionRepository;
    @Autowired
    private IHechoRepository hechoRepository;

    @Override
    public List<OutputColeccionDTO> getAll() {
        return null;
//        return coleccionRepository.getAll().stream()
//                .map(UtilsDTO::toOutputColeccion)
//                .toList();
    }

    @Override
    public List<OutputHecho> getHechosByColeccion(Long id) {
        Coleccion coleccion = coleccionRepository.getById(id);

        if (coleccion == null) {
            throw new NotFoundError("Colección no encontrada con ID: " + id);
        }

        return coleccion.getIdsHechos().stream() // consigo los IDs de los hechos de la coleccion
                .map(idHecho -> hechoRepository.getById(idHecho)) // consigo los hechos con los IDs
                .map(UtilsDTO::hechoToDtoOutput) // los transformo a output
                .toList();
    }

    @Override
    public void guardarHecho(InputColeccionDTO coleccionDTO) {
        Coleccion coleccion = UtilsDTO.toColeccion(coleccionDTO);
        coleccionRepository.guardarColeccion(coleccion);
    }
}
