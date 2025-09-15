package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputAgregadorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IArchivoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ArchivoService implements IArchivoService {
  private IArchivoRepository archivoRepository;
  private IHechoRepository hechoRepository;

  private final WebClient webClient = WebClient
      .builder()
      .baseUrl("http://localhost:8082")
      .build();

  public ArchivoService(IArchivoRepository archivoRepository, IHechoRepository hechoRepository) {
    this.archivoRepository = archivoRepository;
    this.hechoRepository = hechoRepository;
  }

  @Override
  public void guardarArchivo(Archivo archivo) {
    archivoRepository.findByNombre(archivo.getNombre())
            .stream()
            .findFirst()
            .ifPresent(a -> archivo.setId(a.getId()));
    archivoRepository.save(archivo);
    importarHechosArchivo(archivo);
    ArchivoOutputAgregadorDTO outputAgregadorDTO = UtilsDTO.toOutputArchivoAgregador(archivo);
    this.webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/fuentes").build())
        .bodyValue(outputAgregadorDTO)
        .retrieve()
        .toBodilessEntity()
        .subscribe();
  }

  @Override
  public List<Archivo> obtenerArchivos() {
    return this.archivoRepository.findAll();
  }

  private void importarHechosArchivo(Archivo archivo) {
    archivo.importarHechos()
            .doOnNext(this::saveHecho)
            .blockLast(); // Para que espere a que termine
  }

  private void saveHecho(HechoEstatica hecho) {
    this.hechoRepository.findByTitulo(hecho.getTitulo())
            .stream()
            .findFirst()
            .ifPresent(h -> hecho.setId(h.getId()));
    this.hechoRepository.save(hecho);
  }
}
