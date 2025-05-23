package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AgregadorService implements IAgregadorService {
  private IFuenteService fuenteEstatica;
  private IFuenteService fuenteDinamica;
  private IFuenteService fuenteProxy;

  private IColeccionService coleccionService;
  private IHechoService hechoService;

  // Constructor
  public AgregadorService(
      IFuenteService fuenteEstatica,
      IFuenteService fuenteDinamica,
      IFuenteService fuenteProxy,
      IColeccionService coleccionService,
      IHechoService hechoService
  ) {
    this.fuenteEstatica = fuenteEstatica;
    this.fuenteDinamica = fuenteDinamica;
    this.fuenteProxy = fuenteProxy;
    this.coleccionService = coleccionService;
    this.hechoService = hechoService;
  }

  @Override
  public List<HechoOutputDTO> buscarHechos(){
    // TODO: Hacer màs versatil el sistema para que acepte, como no servicios
    List<Hecho> hechosEstatica = this.fuenteEstatica.buscarHechos();
    List<Hecho> hechosDinamica = this.fuenteDinamica.buscarHechos();
    List<Hecho> hechosProxy = this.fuenteProxy.buscarHechos();

    List<HechoOutputDTO> hechosDTO = new ArrayList<>();

    List<HechoOutputDTO> hechosDinamicaDTO = this.hechoService.mapHechoToDTO(hechosDinamica);
    List<HechoOutputDTO> hechosEstaticaDTO = this.hechoService.mapHechoToDTO(hechosEstatica);
    List<HechoOutputDTO> hechosProxyDTO = this.hechoService.mapHechoToDTO(hechosProxy);

    hechosDTO.addAll(hechosEstaticaDTO);
    hechosDTO.addAll(hechosDinamicaDTO);
    hechosDTO.addAll(hechosProxyDTO);

    return hechosDTO;
  }

  @Override
  public List<String> incorporarHecho(HechoInputDTO hechoDTO) {
    Hecho hecho = this.hechoService.DTOToHecho(hechoDTO);
    List<String> nombresColecciones = this.coleccionService.incorporarHecho(hecho);
    if(!nombresColecciones.isEmpty()){
      this.hechoService.guardarHecho(hecho);
    }
    return nombresColecciones;
  }
}
