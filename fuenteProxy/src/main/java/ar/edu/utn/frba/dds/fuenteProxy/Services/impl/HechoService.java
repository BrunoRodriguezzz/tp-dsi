package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HechoService implements IHechoService {
    private IFuenteRepository fuente; //TODO Podría implementar un DAO para cada uno.
    private IHechoRepository hechoRepository;

    public HechoService(IHechoRepository hechoRepository, IFuenteRepository fuentesRepository) {
        this.hechoRepository = hechoRepository;
        this.fuente = fuentesRepository;
    }

    @Override
    public List<OutputFuente> getAll() { //TODO: paginado de GET
        List<OutputFuente> outputFuentes = new ArrayList<>();
        List<Long> ids = fuente.devolverIDs();
        ids.forEach(id -> {
            List<HechoProxy> hechos = hechoRepository.getByIdFuente(id); // Devuelve los hechos con ese ID Fuente
            if (!hechos.isEmpty()) { // Tengo que agregarlos
                OutputFuente outputFuente = new OutputFuente();
                String nombreFuente = this.fuente.getById(id).getNombre();
                List<OutputHecho> hechosOutput = hechos.stream().map(this::hechoToDtoOutput).toList();

                outputFuente.setHechos(hechosOutput);
                outputFuente.setId(id);
                outputFuente.setNombre(nombreFuente);
                outputFuentes.add(outputFuente);
            }
        });
        return outputFuentes;
    }

    @Override
    public List<OutputHecho> getWithFilters(FiltroProxy filtro) {
        List<HechoProxy> hechos = hechoRepository.getWithFilters(filtro);

        return hechos.stream().map(this::hechoToDtoOutput).toList();
    }

    @Override
    public List<OutputHecho> getAllFuente(Long fuenteId) { //TODO
        return List.of();
    }

    @Override
    public void delete(Long idHecho) { //TODO
        hechoRepository.delete(idHecho);
    }

    @Override
    public void guardarHecho(InputHecho hechoDTO) {
        HechoProxy hecho = toHechoProxy(hechoDTO);
        hechoRepository.guardarHecho(hecho);
    }

    private OutputHecho hechoToDtoOutput(HechoProxy hecho) {
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

    private HechoProxy toHechoProxy(InputHecho input) {
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
