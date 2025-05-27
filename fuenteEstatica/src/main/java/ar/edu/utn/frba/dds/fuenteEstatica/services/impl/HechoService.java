package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.domain.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl.HechoRepositoryMemory;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IHechoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechoService implements IHechoService {
    private final IHechoRepository hechoRepository;
    private final IArchivoRepository archivoRepository;

    public HechoService(HechoRepositoryMemory hechoRepository, IArchivoRepository archivoRepository) {
        this.hechoRepository = hechoRepository;
        this.archivoRepository = archivoRepository;
    }

    @Override
    public List<ArchivoOutputDTO> getAll() {
        return this.archivoRepository.getAll().stream().map(this::archivoToDTO).toList();
//        return this.hechoRepository.findAll(this.archivoRepository.getAll());
//        List<HechoEstatica> hechos = hechoRepository.findAll();
//        return hechos.stream().map(this::hechoToDTO).toList();
    }

    @Override
    public HechoOutputDTO getById(Long id) {
        return this.hechoToDTO(this.hechoRepository.getById(id));
    }

    @Override
    public HechoOutputDTO crearHecho(HechoEstatica hecho) {
        return this.hechoToDTO(this.hechoRepository.save(hecho));
    }

    @Override
    public void deleteHecho(Long id) {
        this.hechoRepository.delete(id);
    }

    public ArchivoOutputDTO archivoToDTO(Archivo archivo){
        ArchivoOutputDTO dto = new ArchivoOutputDTO();
        dto.setNombre(archivo.getNombre());
        dto.setHechos(this.hechoRepository.getHechos(archivo));
        return dto;
    }

    private HechoOutputDTO hechoToDTO(HechoEstatica hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setIdHecho(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());

        dto.setLatitud(hecho.getLatitud());
        dto.setLongitud(hecho.getLongitud());

        if (hecho.getFechaHecho() != null)
            dto.setFechaHecho(hecho.getFechaHecho().toString());
        if (hecho.getFechaCreacion() != null)
            dto.setCreatedAt(hecho.getFechaCreacion().toString());
        if (hecho.getFechaModificacion() != null)
            dto.setUpdatedAt(hecho.getFechaModificacion().toString());

        dto.setIdFuente(hecho.getIdFuente());
        dto.setOrigen(hecho.getOrigen());
        return dto;
    }
}
