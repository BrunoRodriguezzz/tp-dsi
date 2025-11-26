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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {
    private final IHechoRepository hechoRepository;
    private final IArchivoRepository archivoRepository;

    public HechoService(IHechoRepository hechoRepository, IArchivoRepository archivoRepository) {
        this.hechoRepository = hechoRepository;
        this.archivoRepository = archivoRepository;
    }

    private List<Long> devolverArchivoID() {
        return archivoRepository.findAll().stream().map(Archivo::getId).toList();
    }

    @Override
    public ArchivoOutputDTO getHechoById(Long id) {
        HechoEstatica hecho = this.hechoRepository.findById(id)
                .orElseThrow(() -> new NotFoundError("Hecho NO encontrado con ID: " + id));
        List<HechoEstatica> hechos = new ArrayList<>();
        hechos.add(hecho);

        return UtilsDTO.toOutputArchivo(this.archivoRepository.getById(hecho.getIdArchivo()), hechos);
    }

    @Override
    public List<ArchivoOutputDTO> getWithFilters(FiltroEstatica filtro) {
        filtro.validate();

        if (filtro.getIdHecho() != null) {
            return buscarPorIdHecho(filtro.getIdHecho());
        }

        List<Long> archivoIds = this.devolverArchivoID();

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

    @Override
    public ArchivoOutputDTO getByFuenteId(Long id, FiltroEstatica filtroEstatica) {
        if(id == null || id <= 0) {
            throw new ValidationError("ID invalido");
        }

        Archivo archivo = archivoRepository.findById(id).orElse(null);
        if(archivo == null) {
            throw new NotFoundError("Archivo no encontrado");
        }

        List<HechoEstatica> hechos;
        if(filtroEstatica.nuevos()) {
            hechos = this.hechoRepository.findAll(HechoSpecification.nuevos(archivo.getUltimaConsulta(), id));
            archivo.setUltimaConsulta(LocalDateTime.now());
            archivoRepository.save(archivo);
        } else {
            hechos = this.hechoRepository.findAll(HechoSpecification.conFiltro(filtroEstatica));
        }

        return UtilsDTO.toOutputArchivo(archivo, hechos);
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
}
