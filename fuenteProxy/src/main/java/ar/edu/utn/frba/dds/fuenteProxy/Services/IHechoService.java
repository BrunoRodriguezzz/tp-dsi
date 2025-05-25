package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IHechoService {
    public List<OutputFuente> getAll();
    public List<OutputHecho> getWithFilters(FiltroProxy filtro);
    public List<OutputHecho> getAllFuente(Long fuenteId);
    public void delete(Long idHecho);
    public void guardarHecho(InputHecho hechoDTO);
}
