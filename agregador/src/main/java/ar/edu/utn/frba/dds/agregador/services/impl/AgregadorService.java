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
import java.lang.invoke.ConstantBootstraps;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgregadorService implements IAgregadorService {
  private IFuenteService fuenteService;
  private IColeccionService coleccionService;
  private IHechoService hechoService;
  private LocalDateTime ultimaFechaRefresco;

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
    this.ultimaFechaRefresco = LocalDateTime.now().minusDays(1); // TODO: Debería persistirse
  }

  @Override
  public List<HechoOutputDTO> buscarHechos(){
    List<Hecho> hechos = this.fuenteService.buscarHechos();
    List<HechoOutputDTO> hechosDTO = UtilsDTO.mapHechoToDTO(hechos);
    return hechosDTO;
  }

  @Override
  public List<String> incorporarHecho(HechoInputDTO hechoDTO) {
    Contribuyente contribuyente = null;
    if(hechoDTO.getContribuyente() != null) {
      try {
        contribuyente = new Contribuyente(
            hechoDTO.getContribuyente().getNombre(),
            hechoDTO.getContribuyente().getApellido(),
            hechoDTO.getContribuyente().getFechaNacimiento()
        );
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    Hecho hecho = UtilsDTO.DTOToHecho(hechoDTO, contribuyente);
//    Hecho aux = this.hechoService.buscarHecho(hecho.getId()); // TODO: Null pointer exception?
//    if(aux != null && hecho.equals(aux)) {
//      throw new HechoYaExistenteException("El hecho ingresado ya existe", aux);
//    }
    List<String> nombresColecciones = this.coleccionService.incorporarHecho(hecho);
    if(!nombresColecciones.isEmpty()){
      this.hechoService.guardarHecho(hecho);
    }
    return nombresColecciones;
  }

  @Override
  public void refrescarColecciones(){
    // TODO: Falta la lógica de nuevos hechos en la request
    List<Hecho> nuevosHechos = this.fuenteService.buscarNuevosHechos(this.ultimaFechaRefresco); // Cambiar por buscar nuevos
    this.coleccionService.incorporarHechos(nuevosHechos);
    this.ultimaFechaRefresco = LocalDateTime.now();
  }
}
