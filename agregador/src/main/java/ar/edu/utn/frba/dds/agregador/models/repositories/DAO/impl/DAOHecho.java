package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOHecho;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOHecho implements IDAOHecho {
  private List<Hecho> hechos;

  public DAOHecho() {this.hechos = new ArrayList<Hecho>();}

  public Hecho save(Hecho hecho) {
    Optional<Hecho> existente = this.hechos.stream()
        .filter(h -> h.getId().equals(hecho.getId()))
        .findFirst();
    if (existente.isPresent()) {
      Hecho hechoExistente = existente.get();

      hechoExistente.setTitulo(hecho.getTitulo());
      hechoExistente.setIdInternoFuente(hecho.getIdInternoFuente());
      hechoExistente.setDescripcion(hecho.getDescripcion());
      hechoExistente.setContribuyente(hecho.getContribuyente());
      hechoExistente.setUbicacion(hecho.getUbicacion());
      hechoExistente.setEtiquetas(hecho.getEtiquetas());
      hechoExistente.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
      hechoExistente.setFechaCarga(hecho.getFechaCarga());
      hechoExistente.setOrigen(hecho.getOrigen());
      hechoExistente.setContribuyente(hecho.getContribuyente());
      hechoExistente.setEstaEliminado(hecho.getEstaEliminado());
      if(hecho.getFuente() != null){
        hechoExistente.setFuente(hecho.getFuente());
      }
      hechoExistente.setContenidoMultimedia(hecho.getContenidoMultimedia());

      return hechoExistente;
    } else {
      this.hechos.add(hecho);
      return hecho;
    }
  }

  public Boolean saveAll(List<Hecho> hechos) {
    this.hechos.addAll(hechos);
    return true;
  }

  public Hecho findById(Long id) {
    Hecho hecho = this.hechos.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    return hecho;
  }

  @Override
  public List<Hecho> findAll() {
    return this.hechos;
  }

  public Hecho findExistent(Hecho hecho) {
    Hecho hechoExistente = this.hechos.stream().filter(h ->
                h.getIdInternoFuente().equals(hecho.getIdInternoFuente()) &&
                h.getOrigen().equals(hecho.getOrigen())
        ).findFirst().orElse(null);
    return hechoExistente;
  }

  public List<Hecho> findByFuente(Fuente fuente) {
    List<Hecho> hechos = this.hechos.stream().filter(h -> h.getFuente().getId().equals(fuente.getId())).toList();
    return hechos;
  }
}
