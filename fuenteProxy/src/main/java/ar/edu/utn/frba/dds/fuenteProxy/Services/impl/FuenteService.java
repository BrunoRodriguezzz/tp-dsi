package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputFuenteDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuenteAgregador;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FuenteService implements IFuenteService {
  @Autowired
  private IFuenteRepository fuenteRepository;
  @Autowired
  private IHechoService hechoService;

  @Value("${servicio.agregador}")
  private String urlAgregador;

  private final WebClient webClient = WebClient
      .builder()
      .baseUrl(urlAgregador)
      .build();

  @Override
  public void guardarFuenteInput(InputFuenteDTO inputFuenteDTO) {
    Fuente fuente = UtilsDTO.toFuente(inputFuenteDTO);
    if (fuente == null) {
      throw new RuntimeException("Fuente no encontrada");
    }
    if (fuente.getNombre() == null || fuente.getNombre().isEmpty()) {
      throw new RuntimeException("Nombre de fuente inválido");
    }
    this.guardarFuente(fuente);
  }

  @Override
  public void guardarFuente(Fuente fuente) {
    this.fuenteRepository.findByNombre(fuente.getNombre()).stream()
            .findFirst()
            .ifPresent(f -> fuente.setId(f.getId()));
    this.fuenteRepository.save(fuente); // Estoy agregando una fuente --> Tengo que avisarle al Agregador de esta nueva fuente
    fuente.getAllHechos()
            .doOnNext(hechoService::guardarHecho)
            .blockLast(); // Para que espere a que termine
    OutputFuenteAgregador fuenteOutputDTO = UtilsDTO.toOutputFuenteAgregador(fuente);
    this.webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/fuentes").build())
        .bodyValue(fuenteOutputDTO)
        .retrieve()
        .toBodilessEntity()
        .subscribe();
  }
}
