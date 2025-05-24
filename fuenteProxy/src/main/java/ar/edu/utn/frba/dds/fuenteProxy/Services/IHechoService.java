package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;

import java.util.Date;
import java.util.List;

public interface IHechoService {
    public List<HechoDTO> getAll();
    public List<HechoDTO> getNew(Date fecha);
    public HechoDTO getById(Long id);
    public List<HechoDTO> getAllFuente(Long fuenteId);
    public void delete(Long idHecho);
}
