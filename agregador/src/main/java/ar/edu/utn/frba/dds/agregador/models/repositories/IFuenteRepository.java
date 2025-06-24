package ar.edu.utn.frba.dds.agregador.models.repositories;


import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import java.util.List;

public interface IFuenteRepository {
  public Fuente guardarFuente(Fuente fuente);
  public Fuente buscarFuente(Long id);
  public Fuente buscarFuente(String nombre);
  public Fuente buscarFuenteNombre(String nombre);
  public List<Fuente> buscarFuentes();
}
