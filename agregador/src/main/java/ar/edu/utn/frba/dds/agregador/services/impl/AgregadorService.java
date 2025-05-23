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
  private IFuenteService fuenteService;

  private IColeccionService coleccionService;
  private IHechoService hechoService;

  // Constructor
  public AgregadorService(
      IFuenteService fuenteService,
      IColeccionService coleccionService,
      IHechoService hechoService
  ) {
    this.fuenteService = fuenteService;
    this.coleccionService = coleccionService;
    this.hechoService = hechoService;
  }

  @Override
  public List<HechoOutputDTO> buscarHechos(){
    // TODO: Hacer màs versatil el sistema para que acepte, como no servicios
    List<Hecho> hechos = this.fuenteService.buscarHechos();

    List<HechoOutputDTO> hechosDTO = this.hechoService.mapHechoToDTO(hechos);

    hechosDTO.addAll(hechosDTO);

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

  @Override
  public void refrescarColecciones(){
    List<Hecho> nuevosHechos = this.fuenteService.nuevosHechos();
    this.coleccionService.incorporarHechos(nuevosHechos);
  }
}
