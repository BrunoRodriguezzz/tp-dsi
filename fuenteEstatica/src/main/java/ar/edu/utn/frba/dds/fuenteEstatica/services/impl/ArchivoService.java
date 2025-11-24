package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.clients.AgregadorClient;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ArchivoService implements IArchivoService {
  private static final Logger logger = LoggerFactory.getLogger(ArchivoService.class);
  private final IArchivoRepository archivoRepository;
  private final IHechoRepository hechoRepository;
  private final UtilsDTO utilsDTO;
  private final AgregadorClient agregadorClient;

  public ArchivoService(IArchivoRepository archivoRepository, IHechoRepository hechoRepository, @Value("${servicio.estatica}") String urlEstatica, AgregadorClient agregadorClient) {
    this.archivoRepository = archivoRepository;
    this.hechoRepository = hechoRepository;
    this.utilsDTO = new UtilsDTO(urlEstatica);
    this.agregadorClient = agregadorClient;
  }

  @Override
  @Async
  public void guardarArchivo(Archivo archivo) {
      archivoRepository.findByNombre(archivo.getNombre())
          .stream()
          .findFirst()
          .ifPresent(a -> archivo.setId(a.getId()));

      archivoRepository.save(archivo);
      importarHechosArchivo(archivo);

      ArchivoOutputAgregadorDTO outputAgregadorDTO = this.utilsDTO.toOutputArchivoAgregador(archivo);

      try {
        this.agregadorClient.incorporarFuente(outputAgregadorDTO);
      } catch (Exception e) {
        logger.warn("Hubo un inconveniente al comunicar con el Agregador: {}", e.getMessage());
      }
  }


  @Override
  public List<Archivo> obtenerArchivos() {
    return this.archivoRepository.findAll();
  }

  private void importarHechosArchivo(Archivo archivo) {
    archivo.importarHechos()
        .doOnNext(this::saveHecho)
        .blockLast();
  }

  private void saveHecho(HechoEstatica hecho) {
    // Buscar duplicados solo dentro del mismo archivo, o sea por título e idArchivo
    this.hechoRepository.findByTituloAndIdArchivo(hecho.getTitulo(), hecho.getIdArchivo())
        .stream()
        .findFirst()
        .ifPresent(h -> hecho.setId(h.getId()));
    this.hechoRepository.save(hecho);
  }
}
