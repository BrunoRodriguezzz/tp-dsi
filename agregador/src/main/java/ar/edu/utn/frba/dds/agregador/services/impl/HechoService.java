package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {
  @Autowired
  private IHechoRepository hechoRepository;

  private IFuenteService fuenteService;

  public HechoService(IFuenteService fuenteService) {
    this.fuenteService = fuenteService;
  }

  @Override
  public List<HechoOutputDTO> buscarHechos() {
    List<Hecho> hechos = this.fuenteService.buscarHechos();
    // Se guardan tambien los de la proxy, si
    hechos = this.guardarHechos(hechos);
    List<HechoOutputDTO> hechosDTO = HechoOutputDTO.mapHechoToDTO(hechos);
    return hechosDTO;
  }

  @Override
  public Hecho incorporarHecho(HechoInputDTO hechoDTO) {
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
    Fuente fuente = this.fuenteService.buscarFuente(hechoDTO.getFuente());
    Hecho hecho = HechoInputDTO.DTOToHecho(hechoDTO, contribuyente, fuente);
    Hecho hechoGuardado = this.guardarHecho(hecho);
    return hechoGuardado;
  }

  @Override
  public Hecho buscarHecho(Long id) {
    Hecho hecho = this.hechoRepository.buscarHecho(id);
    return hecho;
  }

  @Override
  public Hecho guardarHecho(Hecho hecho) {
    Hecho hechoGuardado = this.hechoRepository.guardarHecho(hecho);
    return hechoGuardado;
  }

  @Override
  public List<Hecho> guardarHechos(List<Hecho> hechos){
    List<Hecho> hechosGuardados = this.hechoRepository.guardarHechos(hechos);
    return hechosGuardados;
  }
}
