package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.exceptions.NotFoundError;
import ar.edu.utn.frba.dds.fuenteProxy.models.exceptions.ValidationError;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.HechoSpecification;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {
    private IFuenteRepository fuenteRepository; //TODO Podría implementar un DAO para cada uno.
    private IHechoRepository hechoRepository;

    public HechoService(IHechoRepository hechoRepository, IFuenteRepository fuentesRepository) {
        this.hechoRepository = hechoRepository;
        this.fuenteRepository = fuentesRepository;
    }

    @Override
    public List<OutputFuente> getAll() { //TODO: paginado de GET
        List<OutputFuente> outputFuentes = new ArrayList<>();
        List<Long> ids = this.devolverFuenteID();
        ids.forEach(id -> {
            List<HechoProxy> hechos = hechoRepository.findByIdFuente(id); // Devuelve los hechos con ese ID Fuente
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
                ? this.devolverFuenteID()
                : List.of(filtro.getFuenteId());

        return fuenteIds.stream()
                .map(id -> {
                    List<HechoProxy> hechos = hechoRepository.findAll(HechoSpecification.conFiltro(filtro));
                    Optional<Fuente> fuente = fuenteRepository.findById(id);
                    Fuente fuenteAux;
                    if (fuente.isPresent()) {
                        fuenteAux = fuente.get();
                    }
                    else {
                        throw new NotFoundError("Fuente no encontrada con ID: " + id);
                    }

                    return UtilsDTO.toOutputFuente(fuenteAux, hechos);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<OutputFuente> buscarPorIdHecho(Long idHecho) {
        Optional<HechoProxy> hecho = hechoRepository.findById(idHecho);
        HechoProxy hechoAux;
        if (hecho.isPresent()) {
            hechoAux = hecho.get();
        }
        else {
            throw new NotFoundError("Hecho no encontrada con ID: " + idHecho);
        }

        Optional<Fuente> fuente = fuenteRepository.findById(hechoAux.getIdFuente());
        Fuente fuenteAux;
        if (fuente.isPresent()) {
            fuenteAux = fuente.get();
        }
        else {
            throw new NotFoundError("Fuente no encontrada con ID: " + hechoAux.getIdFuente());
        }

        OutputHecho hechoDTO = UtilsDTO.hechoToDtoOutput(hechoAux);

        OutputFuente output = new OutputFuente();
        output.setId(fuenteAux.getId());
        output.setNombre(fuenteAux.getNombre());
        output.setHechos(List.of(hechoDTO));

        return List.of(output);
    }

    private void toOutputFuente(List<OutputFuente> outputFuentes, Long id, List<HechoProxy> hechos) {
        if (!hechos.isEmpty()) { // Tengo que agregarlos
            OutputFuente outputFuente = new OutputFuente();
            Optional<Fuente> fuente = fuenteRepository.findById(id);
            String nombreFuente;
            if (fuente.isPresent()) {
                nombreFuente = fuente.get().getNombre();
            }
            else {
                throw new NotFoundError("Fuente no encontrada con ID: " + id);
            }

            // TODO: Sacar esto es solo para las pruebas porque sino rompe la reuqest
            List<OutputHecho> hechosOutput = hechos.stream().map(UtilsDTO::hechoToDtoOutput).toList();

            outputFuente.setHechos(hechosOutput);
            outputFuente.setId(id);
            outputFuente.setNombre(nombreFuente);
            outputFuentes.add(outputFuente);
        }
    }

    private List<Long> devolverFuenteID() {
        return fuenteRepository.findAll().stream().map(Fuente::getId).toList();
    }

    @Override
    public List<OutputHecho> getAllFuente(Long fuenteId) { //TODO
        return List.of();
    }

    @Override
    public void delete(Long id) {
        if (id != null && id >= 1) {
            hechoRepository.findById(id)
                    .ifPresentOrElse(hechoProxy -> {
                        hechoProxy.setEliminado(true);
                        hechoRepository.save(hechoProxy);
                    }, () -> {
                        throw new RuntimeException("No se puede eliminar el hecho");
                    });
        } else {
            throw new ValidationError("ID invalido");
        }
    }

    @Override
    public void guardarHecho(InputHecho hechoDTO) { //TODO Validador
        HechoProxy hechoAGuardar = UtilsDTO.toHechoProxy(hechoDTO);
        hechoRepository.findByIdFuenteAndIdExterno(hechoAGuardar.getIdFuente(), hechoAGuardar.getIdExterno())
                .stream()
                .findFirst()
                .ifPresent(h -> hechoAGuardar.setId(h.getId()));
        hechoRepository.save(hechoAGuardar);
    }

    @Override
    public OutputFuente getByFuenteId(Long id) {
        if(id == null || id <= 0) {
            throw new ValidationError("ID invalido");
        }

        Fuente archivo = fuenteRepository.findById(id).orElse(null);
        if(archivo == null) {
            throw new NotFoundError("Archivo no encontrado");
        }

        List<HechoProxy> hechos = this.hechoRepository.findByIdFuente(id);
        return UtilsDTO.toOutputFuente(archivo, hechos);
    }
}