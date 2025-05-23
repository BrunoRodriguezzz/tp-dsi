package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ContribuyenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.UbicacionOutputDTO;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.util.List;

public interface IHechoService {
  public Hecho guardarHecho(Hecho hecho);
  public Hecho DTOToHecho(HechoInputDTO hechoDTO);
  public HechoOutputDTO HechoToDTO(Hecho hecho);
  public List<HechoOutputDTO> mapHechoToDTO(List<Hecho> hechos);
  public UbicacionOutputDTO UbicacionToDTO(Ubicacion ubicacion);
  public ContribuyenteOutputDTO ContribuyenteToDTO(Contribuyente contribuyente);
  public Hecho buscarHecho(Long id);
}
