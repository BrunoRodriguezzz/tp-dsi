package ar.edu.utn.frba.dds.agregador.models.repositories;


import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import java.util.List;

public interface IFuenteRepository {
  public Fuente guardarFuente(Fuente fuente);
  public Boolean inicializarFuentes(List<Fuente> fuentes);
  public Fuente buscarFuenteID(Long id);
  public List<Fuente> guardarFuente(List<Fuente> hechos);
  public Fuente buscarFuenteNombre(String nombre);
}
