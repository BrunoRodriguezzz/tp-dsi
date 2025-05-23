package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.Collections;
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

  public ColeccionOutputDTO buscarColeccion(String id) {
    Coleccion coleccion = this.coleccionRepository.buscarColeccion(id);
    ColeccionOutputDTO coleccionDTO = this.coleccionToDTO(coleccion);
    return coleccionDTO;
  }

  public List<String> incorporarHecho(Hecho hecho) {
    List<Coleccion> colecciones = this.coleccionRepository.buscarColecciones();
    List<String> nombreColecciones = new ArrayList<>();
    colecciones.forEach(coleccion -> {
      List<Hecho> listaHecho = Collections.singletonList(hecho);
      boolean resultado = coleccion.cargarHecho(hecho);
      if (resultado) {
        nombreColecciones.add(coleccion.getTitulo());
      }
    });
    Boolean resultado = this.coleccionRepository.guardarColecciones(colecciones);
    if(!resultado){
      System.out.println("No se pudo guardar las colecciones");
    }
    return nombreColecciones;
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
