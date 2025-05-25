package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

import java.util.List;

public interface IDinamicaService {

    public List<HechoOutputDTO> buscarHechos(Boolean noEnviado);
    public SolicitudOutputDTO   crear(HechoInputDTO hechoInputDTO);
    public void                 eliminar(Long id);
    public SolicitudOutputDTO   actualizar(HechoModificadoInputDTO hechoModificado);
    public Boolean              verificarUsuarioRegistrado(Contribuyente contribuyente);
    public Boolean              verificarEdadNecesaria(HechoInputDTO hechoInputDTO);
    public HechoOutputDTO       gestionarHecho(HechoRevisadoInputDTO hechoRevisado);
}