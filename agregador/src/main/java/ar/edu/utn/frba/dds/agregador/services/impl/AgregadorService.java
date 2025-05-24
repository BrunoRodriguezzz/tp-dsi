package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.HechoYaExistenteException;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgregadorService implements IAgregadorService {
  private IFuenteService fuenteService;
  private IColeccionService coleccionService;
  private IHechoService hechoService;

  @Autowired
  private IContribuyenteRepository contribuyenteRepository;

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
    // TODO: Hacer que la fuente service busque de todas las fuentes
    List<Hecho> hechos = this.fuenteService.buscarHechos();
    List<HechoOutputDTO> hechosDTO = UtilsDTO.mapHechoToDTO(hechos);
    return hechosDTO;
  }

  @Override
  public List<String> incorporarHecho(HechoInputDTO hechoDTO) {
    Contribuyente contribuyente = this.contribuyenteRepository.buscarContribuyente(hechoDTO.getIdContribuyente());
    Hecho hecho = UtilsDTO.DTOToHecho(hechoDTO, contribuyente);
    Hecho aux = this.hechoService.buscarHecho(hecho.getId());
    if(aux != null && hecho.equals(aux)) {
      throw new HechoYaExistenteException("El hecho ingresado ya existe", aux);
    }
    List<String> nombresColecciones = this.coleccionService.incorporarHecho(hecho);
    if(!nombresColecciones.isEmpty()){
      this.hechoService.guardarHecho(hecho);
    }
    return nombresColecciones;
  }

  @Override
  public void refrescarColecciones(){
    // TODO: Falta la lógica de nuevos hechos en la request
    List<Hecho> nuevosHechos = this.fuenteService.nuevosHechos();
    this.coleccionService.incorporarHechos(nuevosHechos);
  }
}
