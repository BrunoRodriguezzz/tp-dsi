package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IAPIService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;

import java.time.LocalDateTime;
import java.util.List;

public class APIService implements IAPIService {

    private IRepositoryService dinamicaRepository;

    @Override
    public List<HechoOutputDTO> buscarHechos(Boolean enviado, LocalDateTime filtroDeTiempo) {
        if(enviado != null){
            return dinamicaRepository
                    .mostrarEnviados(enviado,filtroDeTiempo)
                    .stream()
                    .map(this::hechoOutputDTO)
                    .toList();
        }

        return this.dinamicaRepository
                .mostrarTodos(filtroDeTiempo)
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho){
        return HechoOutputDTO.convertir(hecho);
    }
}
