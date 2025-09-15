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
import java.util.Optional;

@Service
public class ColeccionService implements IColeccionService {

    @Autowired
    private IColeccionRepository coleccionRepository;
    @Autowired
    private IHechoRepository hechoRepository;

    @Override
    public List<OutputColeccionDTO> getAll() {

        return coleccionRepository.findAll()
                .stream()
                .map(UtilsDTO::toOutputColeccion)
                .toList();
    }

    @Override
    public List<OutputHecho> getHechosByColeccion(Long id) {
        Optional<Coleccion> coleccion = coleccionRepository.findById(id);
        Coleccion coleccionAux;

        if (coleccion.isPresent()) {
            coleccionAux  = coleccion.get();
        }
        else {
            throw new NotFoundError("Colección no encontrada con ID: " + id);
        }

        return coleccionAux.getHechos().stream() // consigo los IDs de los hechos de la coleccion
                .map(UtilsDTO::hechoToDtoOutput) // los transformo a output
                .toList();
    }

    @Override
    public void guardarColeccion(InputColeccionDTO coleccionDTO) {
        Coleccion coleccion = UtilsDTO.toColeccion(coleccionDTO);
        coleccionRepository.save(coleccion);
    }
}
