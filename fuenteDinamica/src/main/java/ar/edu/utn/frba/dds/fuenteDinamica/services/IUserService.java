package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoEliminarInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudModOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IUserService {

    public SolicitudOutputDTO   crear(HechoInputDTO hechoInputDTO);
    public SolicitudModOutputDTO actualizar(HechoModificadoInputDTO hechoModificado);
    public Boolean              verificarUsuarioRegistrado(HechoModificadoInputDTO hechoModificado);
    public Boolean              verificarEdadNecesaria(HechoInputDTO hechoInputDTO);
    public Boolean              verificarTiempoParaActualizar(HechoModificadoInputDTO hecho);
}