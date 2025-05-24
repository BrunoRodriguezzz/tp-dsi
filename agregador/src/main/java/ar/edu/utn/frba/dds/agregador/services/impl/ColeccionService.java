package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {
  private IHechoService hechoService;

  public ColeccionService(IHechoService hechoService) {
    this.hechoService = hechoService;
  }

  @Autowired
  private IColeccionRepository coleccionRepository;

  public List<ColeccionOutputDTO> buscarColecciones() {
    List <Coleccion> colecciones = this.coleccionRepository.buscarColecciones();
    List <ColeccionOutputDTO> coleccionesDTO = colecciones.stream().map(c -> this.coleccionToDTO(c)).collect(Collectors.toList());
    return coleccionesDTO;
  }

  public ColeccionOutputDTO buscarColeccion(Long id) {
    Coleccion coleccion = this.coleccionRepository.buscarColeccion(id);
    ColeccionOutputDTO coleccionDTO = this.coleccionToDTO(coleccion);
    return coleccionDTO;
  }

  public List<String> incorporarHecho(Hecho hecho) {
    List<Coleccion> colecciones = this.coleccionRepository.buscarColecciones();
    List<String> nombreColecciones = new ArrayList<>();
    colecciones.forEach(coleccion -> {
      if(coleccion.getHechos().stream().noneMatch(h -> h.equals(hecho))) {
        boolean resultado = coleccion.cargarHecho(hecho);
        if (resultado) {
          nombreColecciones.add(coleccion.getTitulo());
        }
      }
    });
    Boolean resultado = this.coleccionRepository.guardarColecciones(colecciones);
    if(!resultado){
      System.out.println("No se pudo guardar en las colecciones");
    }
    return nombreColecciones;
  }

  public void incorporarHechos(List<Hecho> hechos) {
    // TODO Es poco eficiciente pero funciona
    List<Coleccion> colecciones = this.coleccionRepository.buscarColecciones();
    hechos.forEach(hecho -> {
      colecciones.forEach(c -> {
        if(c.getFuentes().contains(hecho.getFuente())) {
          if(c.getHechos()==null) {
            c.cargarHecho(hecho);
          } else if (c.getHechos().stream().noneMatch(h -> h.equals(hecho))){
            c.cargarHecho(hecho);
          }
        }
      });
    });
    Boolean resultado = this.coleccionRepository.guardarColecciones(colecciones);
    if(!resultado){
      System.out.println("No se pudo guardar en las colecciones");
    }
  }

  public Boolean eliminarHecho(Hecho hecho) {
    return this.coleccionRepository.eliminarHecho(hecho);
  }

  ColeccionOutputDTO coleccionToDTO(Coleccion coleccion) {
    ColeccionOutputDTO coleccionDTO = new ColeccionOutputDTO();
    coleccionDTO.setId(coleccion.getId());
    coleccionDTO.setTitulo(coleccion.getTitulo());
    coleccionDTO.setDescripcion(coleccion.getDescripcion());
    coleccionDTO.setHechos(this.hechoService.mapHechoToDTO(coleccion.getHechos()));
    return coleccionDTO;
  }
}
