package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AgregadorService implements IAgregadorService {
  public List<Hecho> buscarHechos(){
    //TODO: Implementar
    return new ArrayList<Hecho>();
  }
}
