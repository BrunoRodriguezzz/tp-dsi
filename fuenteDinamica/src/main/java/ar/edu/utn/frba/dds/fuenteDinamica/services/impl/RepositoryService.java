package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepositoryService implements IRepositoryService {

    @Autowired
    private IDinamicaRepository dinamicaRepository;

    @Override
    public Hecho buscarPorID (Long ID){
        return this.dinamicaRepository.buscarPorID(ID);
    }
    @Override
    public void guardar(Hecho hecho){
        this.dinamicaRepository.guardar(hecho);
    }
    @Override
    public List<Hecho> mostrarTodos(LocalDateTime filtro){
        return this.dinamicaRepository.mostrarTodos(filtro);
    }

    @Override
    public List<Hecho> mostrarEnviados(Boolean enviado,LocalDateTime filtro){
        return this.dinamicaRepository.mostrarEnviados(enviado,filtro);
    }

    @Override
    public void guardarCambios(Hecho hechoOriginal, Hecho hechoCambiado){
        this.dinamicaRepository.guardarCambios(hechoOriginal,hechoCambiado);
    }
}
