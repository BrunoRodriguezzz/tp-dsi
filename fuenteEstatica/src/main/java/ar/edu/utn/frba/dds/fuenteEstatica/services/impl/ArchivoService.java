package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputAgregadorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IArchivoService;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ArchivoService implements IArchivoService {
  private static final Logger logger = LoggerFactory.getLogger(ArchivoService.class);
  private IArchivoRepository archivoRepository;
  private IHechoRepository hechoRepository;
  private final WebClient webClient;
  private final UtilsDTO utilsDTO;

  public ArchivoService(IArchivoRepository archivoRepository, IHechoRepository hechoRepository, @Value("${servicio.agregador}") String urlAgregador,  @Value("${servicio.estatica}") String urlEstatica) {
    this.archivoRepository = archivoRepository;
    this.hechoRepository = hechoRepository;
    this.webClient = WebClient
        .builder()
        .baseUrl(urlAgregador)
        .build();
    this.utilsDTO = new UtilsDTO(urlEstatica);
  }

  @Override
  public void guardarArchivo(Archivo archivo) {
    try {
      archivoRepository.findByNombre(archivo.getNombre())
          .stream()
          .findFirst()
          .ifPresent(a -> archivo.setId(a.getId()));

      archivoRepository.save(archivo);
      importarHechosArchivo(archivo);

      ArchivoOutputAgregadorDTO outputAgregadorDTO = this.utilsDTO.toOutputArchivoAgregador(archivo);
      this.webClient.post()
          .uri(uriBuilder -> {
            URI finalUri = uriBuilder.path("/fuentes").build();
            return finalUri;
          })
          .bodyValue(outputAgregadorDTO)
          .retrieve()
          .toBodilessEntity()
          .doOnError(error -> {
          })
          .subscribe();

    } catch (Exception e) {
      logger.error(e.getMessage());
    }
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
