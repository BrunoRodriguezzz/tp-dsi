package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.NotFoundError;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.ValidationError;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.HechoSpecification;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IHechoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
        List<Long> ids = this.devolverArchivoID();
        ids.forEach(id -> {
            List<HechoEstatica> hechos = hechoRepository.findByIdArchivo(id); // Devuelve los hechos con ese ID Fuente
            toOutputArchivo(outputArchivos, id, hechos);
        });
        return outputArchivos;
    }

    private List<Long> devolverArchivoID() {
        return archivoRepository.findAll().stream().map(Archivo::getId).toList();
    }

    @Override
    public ArchivoOutputDTO getById(Long id) {
        AtomicReference<ArchivoOutputDTO> rta = new AtomicReference<>();
        this.hechoRepository.findById(id).ifPresentOrElse(hechoEstatica -> {
            List<HechoEstatica> hechos = new ArrayList<>();
            hechos.add(hechoEstatica);
            archivoRepository.findById(hechoEstatica.getId()).ifPresent(a -> rta.set(UtilsDTO.toOutputArchivo(a, hechos)));
        }, () -> {throw new NotFoundError("Hecho no encontrado");});
        return rta.get();
    }

    @Override
    public List<ArchivoOutputDTO> getWithFilters(FiltroEstatica filtro) {
        filtro.validate();

        if (filtro.getIdHecho() != null) {
            return buscarPorIdHecho(filtro.getIdHecho());
        }

        List<Long> archivoIds = (filtro.getArchivoId() == null)
                ? this.devolverArchivoID()
                : List.of(filtro.getArchivoId());

        return archivoIds.stream()
                .map(id -> {
                    List<HechoEstatica> hechos = hechoRepository.findAll(HechoSpecification.conFiltro(filtro));
                    Optional<Archivo> archivo = archivoRepository.findById(id);
                    Archivo archivoAux;
                    if (archivo.isPresent()) {
                        archivoAux = archivo.get();
                    }
                    else {
                        throw new NotFoundError("Fuente no encontrada con ID: " + id);
                    }
                    return UtilsDTO.toOutputArchivo(archivoAux, hechos);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void save(HechoEstatica hecho) {
        this.hechoRepository.findByTitulo(hecho.getTitulo())
                .stream()
                .findFirst()
                .ifPresent(h -> hecho.setId(h.getId()));
        this.hechoRepository.save(hecho);
    }

    @Override
    public void deleteHecho(Long id) {
        if (id != null && id >= 1) {
            hechoRepository.findById(id).ifPresentOrElse(h -> {
                h.setEliminado(true);
                hechoRepository.save(h);
            }, () -> {throw new NotFoundError("Hecho no encontrado");});
        }else {
            throw new ValidationError("ID invalido");
        }
    }

    private List<ArchivoOutputDTO> buscarPorIdHecho(Long idHecho) {
        Optional<HechoEstatica> hecho = hechoRepository.findById(idHecho);
        HechoEstatica hechoAux;
        if (hecho.isPresent()) {
            hechoAux = hecho.get();
        }
        else {
            throw new NotFoundError("Hecho no encontrada con ID: " + idHecho);
        }

        Optional<Archivo> archivo = archivoRepository.findById(hechoAux.getIdArchivo());
        Archivo archivoAux;
        if (archivo.isPresent()) {
            archivoAux = archivo.get();
        }
        else {
            throw new NotFoundError("Archivo no encontrada con ID: " + hechoAux.getIdArchivo());
        }

        HechoOutputDTO hechoDTO = UtilsDTO.hechoToOutputDTO(hechoAux);

        ArchivoOutputDTO output = new ArchivoOutputDTO();
        output.setId(archivoAux.getId());
        output.setNombre(archivoAux.getNombre());
        output.setHechos(List.of(hechoDTO));

        return List.of(output);
    }

    private void toOutputArchivo(List<ArchivoOutputDTO> outputFuentes, Long id, List<HechoEstatica> hechos) {
        if (!hechos.isEmpty()) { // Tengo que agregarlos
            ArchivoOutputDTO outputFuente = new ArchivoOutputDTO();
            Optional<Archivo> archivo = archivoRepository.findById(id);
            String nombreArchivo;
            if (archivo.isPresent()) {
                nombreArchivo = archivo.get().getNombre();
            }
            else {
                throw new NotFoundError("Archivo no encontrada con ID: " + id);
            }

            List<HechoOutputDTO> hechosOutput = hechos.stream().map(UtilsDTO::hechoToOutputDTO).toList();

            outputFuente.setHechos(hechosOutput);
            outputFuente.setId(id);
            outputFuente.setNombre(nombreArchivo);
            outputFuentes.add(outputFuente);
        }
    }

//    @Override
//    public HechoOutputDTO crearHecho(HechoEstatica hecho) {
//        return UtilsDTO.hechoToOutputDTO(this.hechoRepository.save(hecho));
//    }

//    @Override
//    public void guardarHecho(InputHechoDTO hechoDTO) { //TODO Validador
//        HechoEstatica hecho = UtilsDTO.toHechoEstica(hechoDTO);
//        hechoRepository.guardarHecho(hecho);
//    }

//    @Override
//    public void guardarHecho(HechoEstatica hecho) {
//        hechoRepository.save(hecho);
//    }
}
