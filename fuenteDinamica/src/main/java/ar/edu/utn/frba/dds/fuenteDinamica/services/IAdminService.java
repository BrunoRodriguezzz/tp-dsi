package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoEliminarInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;

import java.util.List;

public interface IAdminService {
    public HechoOutputDTO gestionarHecho(HechoRevisadoInputDTO hechoRevisado);
    public void           eliminar(Long id);

    List<HechoOutputDTO> obtenerHechosPendientes();
}