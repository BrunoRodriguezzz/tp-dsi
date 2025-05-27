package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.exceptions.ValidationError;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            toOutputFuente(outputFuentes, id, hechos);
        });
        return outputFuentes;
    }

    @Override
    public List<OutputFuente> getWithFilters(FiltroProxy filtro) {
        filtro.validate();

        if (filtro.getIdHecho() != null) {
            return buscarPorIdHecho(filtro.getIdHecho());
        }

        List<Long> fuenteIds = (filtro.getFuenteId() == null)
                ? fuente.devolverIDs()
                : List.of(filtro.getFuenteId());

        return fuenteIds.stream()
                .map(id -> {
                    List<HechoProxy> hechos = hechoRepository.getFiltrados(id, filtro);
                    Fuente fuente = this.fuente.getById(id);
                    return UtilsDTO.toOutputFuente(fuente, hechos);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<OutputFuente> buscarPorIdHecho(Long idHecho) {
        HechoProxy hecho = hechoRepository.getById(idHecho);
        Fuente fuente = this.fuente.getById(hecho.getIdFuente());

        OutputHecho hechoDTO = UtilsDTO.hechoToDtoOutput(hecho);

        OutputFuente output = new OutputFuente();
        output.setId(fuente.getId());
        output.setNombre(fuente.getNombre());
        output.setHechos(List.of(hechoDTO));

        return List.of(output);
    }

    private void toOutputFuente(List<OutputFuente> outputFuentes, Long id, List<HechoProxy> hechos) {
        if (!hechos.isEmpty()) { // Tengo que agregarlos
            OutputFuente outputFuente = new OutputFuente();
            String nombreFuente = this.fuente.getById(id).getNombre();
            List<OutputHecho> hechosOutput = hechos.stream().map(UtilsDTO::hechoToDtoOutput).toList();

            outputFuente.setHechos(hechosOutput);
            outputFuente.setId(id);
            outputFuente.setNombre(nombreFuente);
            outputFuentes.add(outputFuente);
        }
    }

    @Override
    public List<OutputHecho> getAllFuente(Long fuenteId) { //TODO
        return List.of();
    }

    @Override
    public void delete(Long id) {
        if (id != null && id >= 1) {
            hechoRepository.delete(id);
        }else {
            throw new ValidationError("ID invalido");
        }

    }

    @Override
    public void guardarHecho(InputHecho hechoDTO) { //TODO Validador
        HechoProxy hecho = UtilsDTO.toHechoProxy(hechoDTO);
        hechoRepository.guardarHecho(hecho);
    }
}