package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IAPIService {
    public List<HechoOutputDTO> buscarHechos(Boolean noEnviado, LocalDateTime filtro);
    public List<HechoOutputDTO> hechosDeIgualTitulo(Boolean enviado,LocalDateTime dateTimeGT,String titulo);
}
