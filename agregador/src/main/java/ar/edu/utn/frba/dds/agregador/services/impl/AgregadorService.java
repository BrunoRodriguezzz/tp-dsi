package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

@Service
public class AgregadorService implements IAgregadorService {
  // Para evitar relaciones circulares entre servicios
  private IColeccionService coleccionService;
  private IHechoService hechoService;
  private IFuenteService fuenteService;

  public AgregadorService(IColeccionService coleccionService, IHechoService hechoService, IFuenteService fuenteService) {
    this.coleccionService = coleccionService;
    this.hechoService = hechoService;
    this.fuenteService = fuenteService;
  }

  @Override
  public List<String> incorporarHecho(HechoInputDTO hecho) {
    Hecho guardado = this.hechoService.incorporarHecho(hecho);
      return this.coleccionService.incorporarHecho(guardado);
  }

  @Override
  public Fuente incorporarFuente(@RequestBody FuenteInputDTO fuente) {
    Fuente fuenteIncorporada = this.fuenteService.incorporarFuente(fuente);
    this.buscarHechosFuente(fuenteIncorporada);
    return fuenteIncorporada;
  }

  @Async
  public void buscarHechosFuente(Fuente fuenteIncorporada) {
    List<Hecho> hechos;
    if(fuenteIncorporada.getNombre() != null){
      hechos = fuenteService.buscarHechosFuenteStream(fuenteIncorporada.getNombre()).collectList().block();
      if(hechos != null){
        this.hechoService.guardarHechos(hechos);
        this.coleccionService.incorporarHechos(hechos);
      }
    }
    else
      throw new RuntimeException("Se incorporó una fuente sin nombre, no fue posible obtener sus hechos");
  }
}
