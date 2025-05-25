package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.HechoDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl.HechoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IHechoService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class HechoService implements IHechoService {
    private final HechoRepository hechoRepository;

    public HechoService(HechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

    @Override
    public List<HechoDTO> findAll() {
        List<HechoEstatica> hechos = hechoRepository.findAll();
        return hechos.stream().map(this::hechoToDTO).toList();
    }

    private HechoDTO hechoToDTO(HechoEstatica hecho) {
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
