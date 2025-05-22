package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;

@Service
public class AgregadorService implements IAgregadorService {
  private IFuenteService fuenteEstatica;
  private IFuenteService fuenteDinamica;
  private IFuenteService fuenteProxy;

  private IColeccionService coleccionService;

  // Constructor
  public AgregadorService(
      IFuenteService fuenteEstatica,
      IFuenteService fuenteDinamica,
      IFuenteService fuenteProxy,
      IColeccionService coleccionService
  ) {
    this.fuenteEstatica = fuenteEstatica;
    this.fuenteDinamica = fuenteDinamica;
    this.fuenteProxy = fuenteProxy;
    this.coleccionService = coleccionService;
  }

  @Override
  public List<Hecho> buscarHechos(){
    List<Hecho> hechosEstatica = this.fuenteEstatica.buscarHechos();
    List<Hecho> hechosDinamica = this.fuenteDinamica.buscarHechos();
    List<Hecho> hechosProxy = this.fuenteProxy.buscarHechos();

    List<Hecho> hechos = new ArrayList<>();

    hechos.addAll(hechosEstatica);
    hechos.addAll(hechosDinamica);
    hechos.addAll(hechosProxy);

    return hechos;
  }

  @Override
  public List<String> incorporarHecho(HechoInputDTO hechoDTO) {
    Hecho hecho = this.DTOtoHecho(hechoDTO);
    List<String> nombresColecciones = this.coleccionService.incorporarHecho(hecho);
    return nombresColecciones;
  }

  public Hecho DTOtoHecho(HechoInputDTO hechoDTO) {
    Hecho hecho;
    try {
      hecho = new Hecho(
          hechoDTO.getTitulo(),
          hechoDTO.getDescripcion(),
          new Categoria(hechoDTO.getCategoria()),
          new Ubicacion(
              hechoDTO.getUbicacion().getLatitud(),
              hechoDTO.getUbicacion().getLongitud()
          ),
          hechoDTO.getFechaAcontecimiento(),
          Origen.valueOf(hechoDTO.getOrigen().toUpperCase())
      );
      hecho.setFuente(hechoDTO.getFuente());
      hecho.setEstaEliminado(false);
      return hecho;
    } catch (Exception e){
      System.out.println(e.getMessage());
    }
    return null;
  }

}
