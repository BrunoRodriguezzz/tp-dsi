package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IColeccionService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.exceptions.ValidationError;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    @Autowired
    private IColeccionRepository coleccionRepository;
    @Autowired
    private IHechoRepository hechoRepository;

    @Override
    public List<InputColeccionDTO> getAll() { //TODO
        return null;
    }

    @Override
    public List<OutputHecho> getHechosByColeccion(String identificador) { //TODO
        return null;
    }

    @Override
    public void guardarHecho(Coleccion coleccion) { //TODO
    }
}
