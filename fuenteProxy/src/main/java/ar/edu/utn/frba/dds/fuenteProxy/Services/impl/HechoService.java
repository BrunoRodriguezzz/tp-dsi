package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        HechoProxy hecho = toHecho(hechoDTO);
        hechoRepository.guardarHecho(hecho);
    }

    private HechoDTO hechoToDTO(HechoProxy hecho) {
        HechoDTO dto = new HechoDTO();
        dto.setId_hecho(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());

        dto.setLatitud(hecho.getLatitud());
        dto.setLongitud(hecho.getLongitud());

        if (hecho.getFechaHecho() != null)
            dto.setFecha_hecho(hecho.getFechaHecho().toString());
        if (hecho.getFechaCreacion() != null)
            dto.setCreated_at(hecho.getFechaCreacion().toString());
        if (hecho.getFechaModificacion() != null)
            dto.setUpdated_at(hecho.getFechaModificacion().toString());

        dto.setId_fuente(hecho.getIdFuente());
        return dto;
    }

    private HechoProxy toHecho(HechoDTO hechoDTO) {
        HechoProxy hecho = new HechoProxy(hechoDTO.getId_hecho(), hechoDTO.getTitulo());
        hecho.setDescripcion(hechoDTO.getDescripcion());
        hecho.setCategoria(hechoDTO.getCategoria());
        hecho.setLatitud(hechoDTO.getLatitud());
        hecho.setLongitud(hechoDTO.getLongitud());
        hecho.setFechaHecho(hecho.getFechaHecho());
        hecho.setFechaModificacion(hecho.getFechaModificacion());
        hecho.setFechaCreacion(hecho.getFechaCreacion());
        hecho.setEliminado(false);

        return hecho;
    }
}
