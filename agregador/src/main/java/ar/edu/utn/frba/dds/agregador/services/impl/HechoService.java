package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ContribuyenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {
  @Autowired
  IHechoRepository hechoRepository;

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

// DTOs
  @Override
  public Hecho DTOToHecho(HechoInputDTO hechoDTO) {
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

  @Override
  public HechoOutputDTO HechoToDTO(Hecho hecho) {
    HechoOutputDTO hechoDTO = new HechoOutputDTO();
    hechoDTO.setId(hecho.getId());
    hechoDTO.setTitulo(hecho.getTitulo());
    hechoDTO.setDescripcion(hecho.getDescripcion());
    hechoDTO.setCategoria(hecho.getCategoria().getTitulo());
    hechoDTO.setUbicacion(this.UbicacionToDTO(hecho.getUbicacion()));
    hechoDTO.setFechaAcontecimiento(hecho.getFechaAcontecimiento());

    if(hecho.getContribuyente() != null){
      hechoDTO.setContribuyente(this.ContribuyenteToDTO(hecho.getContribuyente()));
    }

    else{
      hechoDTO.setContribuyente(null);
    }

    hechoDTO.setFuente(hecho.getFuente());
    hechoDTO.setOrigen(hecho.getOrigen().name());
    return hechoDTO;
  }

  public List<HechoOutputDTO> mapHechoToDTO(List<Hecho> hechos) {
    if(hechos == null){
      return null;
    }
    List<HechoOutputDTO> hechosDTO =
        hechos.stream()
            .map(this::HechoToDTO)
            .collect(Collectors.toList());
    return hechosDTO;
  }

  @Override
  public UbicacionOutputDTO UbicacionToDTO(Ubicacion ubicacion) {
    UbicacionOutputDTO ubicacionDTO = new UbicacionOutputDTO();
    ubicacionDTO.setLatitud(ubicacion.getLatitud());
    ubicacionDTO.setLongitud(ubicacion.getLongitud());
    return ubicacionDTO;
  }

  @Override
  public ContribuyenteOutputDTO ContribuyenteToDTO(Contribuyente contribuyente) {
    ContribuyenteOutputDTO contribuyenteDTO = new ContribuyenteOutputDTO();
    contribuyenteDTO.setNombre(contribuyente.getNombre());
    contribuyenteDTO.setApellido(contribuyente.getApellido());
    contribuyenteDTO.setFechaNacimiento(contribuyente.getFechaNacimiento());
    return contribuyenteDTO;
  }
}
