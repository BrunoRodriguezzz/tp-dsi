package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.clients.AgregadorClient;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputFuenteDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuenteAgregador;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FuenteService implements IFuenteService {
  @Autowired
  private IFuenteRepository fuenteRepository;
  @Autowired
  private IHechoService hechoService;

  private final UtilsDTO utilsDTO;
  private final AgregadorClient agregadorClient;

  public FuenteService (@Value("${servicio.proxy}") String urlProxy, AgregadorClient agregadorClient) {
    this.utilsDTO = new UtilsDTO(urlProxy);
    this.agregadorClient = agregadorClient;
  }

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
  @Async
  public void guardarFuente(Fuente fuente) {
    this.fuenteRepository.findByNombre(fuente.getNombre()).stream()
            .findFirst()
            .ifPresent(f -> fuente.setId(f.getId()));
    this.fuenteRepository.save(fuente); // Estoy agregando una fuente --> Tengo que avisarle al Agregador de esta nueva fuente
    fuente.getAllHechos()
            .doOnNext(hechoService::guardarHecho)
            .blockLast(); // Para que espere a que termine
    OutputFuenteAgregador fuenteOutputDTO = this.utilsDTO.toOutputFuenteAgregador(fuente);
    try {
      agregadorClient.incorporarFuente(fuenteOutputDTO);
    } catch (Exception e) {
        log.warn("Hubo un inconveniente al comunicar con el Agregador: {}", e.getMessage());
    }
  }
}
