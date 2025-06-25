package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AgregadorService implements IAgregadorService {
  // Para evitar relaciones circulares entre servicios
  private IColeccionService coleccionService;
  private IHechoService hechoService;

  public AgregadorService(IColeccionService coleccionService, IHechoService hechoService) {
    this.coleccionService = coleccionService;
    this.hechoService = hechoService;
  }

  @Override
  public List<String> incorporarHecho(HechoInputDTO hecho) {
    Hecho guardado = this.hechoService.incorporarHecho(hecho);
    List<String> nombresColecciones = this.coleccionService.incorporarHecho(guardado);
    return nombresColecciones;
  }
}
