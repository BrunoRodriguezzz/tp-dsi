package ar.edu.utn.frba.dds.fuenteEstatica.services.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.clients.AgregadorClient;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.UtilsDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputAgregadorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IArchivoService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
      archivoRepository.save(archivo);
      importarHechosArchivo(archivo);

      ArchivoOutputAgregadorDTO outputAgregadorDTO = this.utilsDTO.toOutputArchivoAgregador(archivo);

      try {
        this.agregadorClient.incorporarFuente(outputAgregadorDTO);
      } catch (Exception e) {
        logger.warn("Hubo un inconveniente al comunicar con el Agregador: {}", e.getMessage());
      }
  }

  public void guardarArchivoSync(Archivo archivo) {
      logger.info("💾 Guardando archivo en BD: {}", archivo.getNombre());
      archivoRepository.save(archivo);
      logger.info("✅ Archivo guardado en BD con ID: {}", archivo.getId());

      // Importar hechos de forma síncrona primero (para archivos grandes puede tardar)
      logger.info("📥 Iniciando importación de hechos para archivo: {} (ID: {})", archivo.getNombre(), archivo.getId());
      importarHechosArchivo(archivo);
    
      ArchivoOutputAgregadorDTO outputAgregadorDTO = this.utilsDTO.toOutputArchivoAgregador(archivo);

      try {
        logger.info("📡 Notificando al agregador sobre la nueva fuente: {}", archivo.getNombre());
        this.agregadorClient.incorporarFuente(outputAgregadorDTO);
        logger.info("✅ Agregador notificado exitosamente");
      } catch (Exception e) {
        logger.warn("⚠️ Hubo un inconveniente al comunicar con el Agregador: {}", e.getMessage());
      }
  }


  @Override
  public List<Archivo> obtenerArchivos() {
    return this.archivoRepository.findAll();
  }

  private void importarHechosArchivo(Archivo archivo) {
    logger.info("📥 Iniciando importación de hechos para archivo: {} (ID: {})", archivo.getNombre(), archivo.getId());
    logger.info("📂 Ruta del archivo: {}", archivo.getRutaArchivo());

    archivo.importarHechos()
        .buffer(100) // Procesar en lotes de 100 hechos
        .doOnNext(lote -> {
          logger.info("💾 Guardando lote de {} hechos", lote.size());
          saveLoteHechos(lote);
        })
        .blockLast();

    logger.info("✅ Importación completada para archivo: {}", archivo.getNombre());
  }

  @Async
  private void importarHechosArchivoAsync(Archivo archivo) {
    logger.info("🔄 Importación asíncrona iniciada para: {}", archivo.getNombre());
    logger.info("📂 Ruta del archivo: {}", archivo.getRutaArchivo());

    try {
      archivo.importarHechos()
          .buffer(100) // Procesar en lotes de 100 hechos
          .doOnNext(lote -> {
            logger.info("💾 Guardando lote de {} hechos", lote.size());
            saveLoteHechos(lote);
          })
          .blockLast();

      logger.info("✅ Importación asíncrona completada para archivo: {}", archivo.getNombre());
    } catch (Exception e) {
      logger.error("❌ Error en importación asíncrona para archivo: {}", archivo.getNombre(), e);
    }
  }

  private void saveLoteHechos(List<HechoEstatica> hechos) {
    // Guardar todos los hechos del lote de una vez
    hechoRepository.saveAll(hechos);
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
