package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface IFuenteService {
  public List<Hecho> buscarHechos();
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco);
  public List<Hecho> buscarHechosFuente(TipoFuente tipoFuente);
  public List<Hecho> buscarHechosFuente(String nombre);
  public Fuente buscarFuente(Long id);
  public Fuente buscarFuente(String nombre);
  public Fuente incorporarFuente(FuenteInputDTO fuenteInputDTO);
  public void eliminarHecho(Hecho hecho);
}
