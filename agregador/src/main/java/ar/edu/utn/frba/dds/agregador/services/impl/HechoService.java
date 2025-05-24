package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
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
}
