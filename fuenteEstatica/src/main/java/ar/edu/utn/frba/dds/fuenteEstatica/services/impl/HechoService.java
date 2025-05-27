package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IHechoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {
    private final IHechoRepository hechoRepository;
    private final IArchivoRepository archivoRepository;

    public HechoService(IHechoRepository hechoRepository, IArchivoRepository archivoRepository) {
        this.hechoRepository = hechoRepository;
        this.archivoRepository = archivoRepository;
    }

//    @Override
//    public List<ArchivoOutputDTO> getAll() {
//        List<HechoEstatica> hechos = hechoRepository.getAll();
//        Archivo archivo = archivoRepository.getAll().get(0);
//        ArchivoOutputDTO archivoOutputDTO = UtilsDTO.toOutputArchivo(archivo, hechos);
//        return List.of(archivoOutputDTO);
//    }

    @Override
    public List<ArchivoOutputDTO> getAll() { //TODO: paginado de GET
        List<ArchivoOutputDTO> outputArchivos = new ArrayList<>();
        List<Long> ids = archivoRepository.devolverIDs();
        ids.forEach(id -> {
            List<HechoEstatica> hechos = hechoRepository.getByIdArchivo(id); // Devuelve los hechos con ese ID Fuente
            toOutputArchivo(outputArchivos, id, hechos);
        });
        return outputArchivos;
    }

    @Override
    public List<ArchivoOutputDTO> getWithFilters(FiltroEstatica filtro) {
        filtro.validate();

        if (filtro.getIdHecho() != null) {
            return buscarPorIdHecho(filtro.getIdHecho());
        }

        List<Long> fuenteIds = (filtro.getArchivoId() == null)
                ? this.archivoRepository.devolverIDs()
                : List.of(filtro.getArchivoId());

        return fuenteIds.stream()
                .map(id -> {
                    List<HechoEstatica> hechos = hechoRepository.getFiltrados(id, filtro);
                    Archivo archivo = this.archivoRepository.getById(id);
                    return UtilsDTO.toOutputArchivo(archivo, hechos);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<ArchivoOutputDTO> buscarPorIdHecho(Long idHecho) {
        HechoEstatica hecho = hechoRepository.getById(idHecho);
        Archivo archivo = this.archivoRepository.getById(hecho.getIdArchivo());

        HechoOutputDTO hechoDTO = UtilsDTO.hechoToOutputDTO(hecho);

        ArchivoOutputDTO output = new ArchivoOutputDTO();
        output.setId(archivo.getId());
        output.setNombre(archivo.getNombre());
        output.setHechos(List.of(hechoDTO));

        return List.of(output);
    }

    private void toOutputArchivo(List<ArchivoOutputDTO> outputFuentes, Long id, List<HechoEstatica> hechos) {
        if (!hechos.isEmpty()) { // Tengo que agregarlos
            ArchivoOutputDTO outputFuente = new ArchivoOutputDTO();
            String nombreFuente = this.archivoRepository.getById(id).getNombre();
            List<HechoOutputDTO> hechosOutput = hechos.stream().map(UtilsDTO::hechoToOutputDTO).toList();

            outputFuente.setHechos(hechosOutput);
            outputFuente.setId(id);
            outputFuente.setNombre(nombreFuente);
            outputFuentes.add(outputFuente);
        }
    }

    @Override
    public void guardarHecho(HechoEstatica hecho) {
        hechoRepository.save(hecho);
    }

    @Override
    public HechoOutputDTO getById(Long id) {
        return UtilsDTO.hechoToOutputDTO(this.hechoRepository.getById(id));
    }

    @Override
    public HechoOutputDTO crearHecho(HechoEstatica hecho) {
        return UtilsDTO.hechoToOutputDTO(this.hechoRepository.save(hecho));
    }

    @Override
    public void deleteHecho(Long id) {
        this.hechoRepository.delete(id);
    }
}
