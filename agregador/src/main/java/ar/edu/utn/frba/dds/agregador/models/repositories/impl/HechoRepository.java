package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

//@Repository
//public class HechoRepository implements IHechoRepository {
//  private IDAOHecho dao;
//  private final AtomicLong generadorIds = new AtomicLong(1);
//
//  public HechoRepository(){
//    this.dao = new DAOHecho();
//  }
//
//  public Boolean inicializarHechos(List<Hecho> hechos) {
//    return this.dao.saveAll(hechos);
//  }
//
//  public Hecho buscarHecho(Long id) {
//    return this.dao.findById(id);
//  }
//
//  @Override
//  public List<Hecho> buscarHechos() {
//    return this.dao.findAll();
//  }
//
//  public Hecho guardarHecho(Hecho hecho) {
//    Hecho hechoExistente = this.dao.findExistent(hecho);
//    if(hechoExistente != null){
//      hecho.setId(hechoExistente.getId());
//    }
//    else{
//      hecho.setId(generadorIds.getAndIncrement());
//    }
//    Hecho hechoGuardado = this.dao.save(hecho);
//    return hechoGuardado;
//  }
//
//  public List<Hecho> guardarHechos(List<Hecho> hechos) {
//    List <Hecho> hechosNuevos = new ArrayList<>();
//    hechos.forEach(h -> {
//      Hecho hechoGuardado = this.guardarHecho(h);
//      hechosNuevos.add(hechoGuardado);
//    });
//    return hechosNuevos;
//  }
//
//  @Override
//  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente){
//    List<Hecho> hechos = this.dao.findByFuente(fuente);
//    return hechos;
//  }
//}
