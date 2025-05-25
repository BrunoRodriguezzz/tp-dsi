package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IHechoService {
    public List<HechoDTO> getAll();
    public List<HechoDTO> getWithFilters(FiltroProxy filtro);
    public List<HechoDTO> getAllFuente(Long fuenteId);
    public void delete(Long idHecho);
}
