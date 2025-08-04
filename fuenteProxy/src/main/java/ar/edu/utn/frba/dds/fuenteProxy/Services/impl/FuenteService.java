package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputFuenteDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuenteAgregador;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FuenteService implements IFuenteService {
  @Autowired
  private IFuenteRepository fuenteRepository;

  private final WebClient webClient = WebClient
      .builder()
      .baseUrl("http://localhost:8082")
      .build();

  @Override
  public void guardarFuenteInput(InputFuenteDTO inputFuenteDTO) {
    Fuente fuente = UtilsDTO.toFuente(inputFuenteDTO);
    this.guardarFuente(fuente);
  }

  @Override
  public void guardarFuente(Fuente fuente) {
    this.fuenteRepository.save(fuente); // Estoy agregando una fuente --> Tengo que avisarle al Agregador de esta nueva fuente
    OutputFuenteAgregador fuenteOutputDTO = UtilsDTO.toOutputFuenteAgregador(fuente);
    // TODO: Buscar una implementación más linda
    this.webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/fuentes").build())
        .bodyValue(fuenteOutputDTO)
        .retrieve()
        .toBodilessEntity()
        .subscribe();
  }
}
