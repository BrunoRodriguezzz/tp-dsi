package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.IEstadisticaHechosRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.ISolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SeederService implements ApplicationRunner {
  @Autowired
  private IEstadisticaHechosRepository estadisticaHechosRepository;

  @Autowired
  private ICategoriaRepository categoriaRepository;

  @Autowired
  private ISolicitudRepository solicitudRepository;

  @Autowired
  private IColeccionRepository coleccionRepository;

  public void init() {

  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

  }
}