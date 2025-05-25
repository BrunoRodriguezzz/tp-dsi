package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HechoService implements IHechoService {
    private IFuenteRepository fuentes; //TODO Podría implementar un DAO para cada uno.
    private IHechoRepository hechoRepository;

    public HechoService(IHechoRepository hechoRepository, IFuenteRepository fuentesRepository) {
        this.hechoRepository = hechoRepository;
        this.fuentes = fuentesRepository;
    }

    @Override
    public List<HechoDTO> getAll() { //TODO: paginado de GET
        List<HechoProxy> hechos = hechoRepository.getAll();
        return hechos.stream().map(this::hechoToDTO).toList();
    }

    @Override
    public List<HechoDTO> getWithFilters(FiltroProxy filtro) {
        List<HechoProxy> hechos = hechoRepository.getWithFilters(filtro);
        return hechos.stream().map(this::hechoToDTO).toList();
    }

    @Override
    public List<HechoDTO> getAllFuente(Long fuenteId) { //TODO
        return List.of();
    }

    @Override
    public void delete(Long idHecho) { //TODO
        hechoRepository.delete(idHecho);
    }

    @Override
    public void guardarHecho(HechoDTO hechoDTO) {
        HechoProxy hecho = new HechoProxy(hechoDTO.getIdHecho(), hechoDTO.getTitulo());
        hechoRepository.guardarHecho(hecho);
    }

    private HechoDTO hechoToDTO(HechoProxy hecho) {
        HechoDTO dto = new HechoDTO();
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
        return dto;
    }
}
