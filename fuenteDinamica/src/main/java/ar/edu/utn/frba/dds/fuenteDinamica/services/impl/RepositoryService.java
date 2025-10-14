package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RepositoryService implements IRepositoryService {

    private IDinamicaRepository dinamicaRepository;

    public RepositoryService(IDinamicaRepository dinamicaRepository){
        this.dinamicaRepository = dinamicaRepository;
    };

    @Override
    public List<Hecho> buscarTodos(){
        return this.dinamicaRepository.findAll();
    }

    @Override
    public Hecho buscarPorID (Long id){
        List<Hecho> hechos = this.buscarTodos();

        return hechos.stream()
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void guardar(Hecho hecho){
        this.dinamicaRepository.save(hecho);
    }
    @Override
    public List<Hecho> mostrarTodos(LocalDateTime filtroDeTiempo){

        List<Hecho> hechos = this.dinamicaRepository.findAll();

        List<Hecho> hechosQueNoEstanEnviados = hechos
                .stream()
                .filter(hecho -> !hecho.getEnviado()
                        && estadoAceptado(hecho)
                        && !(hecho.getEstaEliminado()))
                .toList();

        for (Hecho hecho : hechosQueNoEstanEnviados) {

            hecho.setEnviado(true);

            this.guardar(hecho);
        }

        if(filtroDeTiempo != null){
            return hechos
                    .stream()
                    .filter(hecho -> estadoAceptado(hecho)
                            && !(hecho.getEstaEliminado())
                            && hecho.getFechaGuardado().isAfter(filtroDeTiempo))
                    .toList();
        }else{
            return hechos
                    .stream()
                    .filter(hecho -> estadoAceptado(hecho)
                            && !(hecho.getEstaEliminado()))
                    .toList();
        }
    }

    @Override
    public List<Hecho> mostrarEnviados(Boolean enviado,LocalDateTime filtro){

        List<Hecho> hechos = this.dinamicaRepository.findAll();

        List<Hecho> hechosSolicitados = hechos
                .stream()
                .filter(hecho -> hecho.getEnviado() == enviado
                        && estadoAceptado(hecho)
                        && !(hecho.getEstaEliminado())
                        && hecho.getFechaGuardado().isAfter(filtro))
                .toList();

        if(!enviado) {
            for (Hecho hecho : hechosSolicitados) {

                hecho.setEnviado(true);

                this.guardar(hecho);
            }
        }
        return hechosSolicitados;
    }

    private Boolean estadoAceptado(Hecho hecho){

        return (hecho.getEstadoHecho() == EstadoHecho.ACEPTADA) || (hecho.getEstadoHecho() == EstadoHecho.ACEPTADA_CON_SUGERENCIA);

    }
}