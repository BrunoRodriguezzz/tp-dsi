package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.SolicitudModificacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.ISolicitudesModRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RepositoryService implements IRepositoryService {

    private IDinamicaRepository dinamicaRepository;
    private ISolicitudesModRepository solicitudesModRepository;

    public RepositoryService(IDinamicaRepository dinamicaRepository, ISolicitudesModRepository solicitudesModRepository) {
        this.dinamicaRepository = dinamicaRepository;
        this.solicitudesModRepository = solicitudesModRepository;
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

    @Override
    public List<Hecho> buscarTodosDeUnUsuario(Long idUsuario){
        return this.dinamicaRepository.findAllByContribuyente_IdUsuario(idUsuario);
    }

    @Override
    public List<Hecho> buscarTodosDeUnContribuyente(Long idContribuyente) {
//        return this.dinamicaRepository.findByContribuyente_Id(idContribuyente);
        return this.dinamicaRepository.findByContribuyente_IdUsuario(idContribuyente);
    }

    @Override
    public List<Hecho> buscarPendientes() {
        return this.dinamicaRepository.findByEstadoHecho(EstadoHecho.PENDIENTE_DE_REVISION);
    }

    @Override
    public Hecho buscarPendientesPorID(Long id) {
        return this.dinamicaRepository
                .findByEstadoHechoAndId(EstadoHecho.PENDIENTE_DE_REVISION, id)
                .orElse(null);
    }

    @Override
    public void guardarSolicitudModificacion(SolicitudModificacion solicitudModificacion){
        this.solicitudesModRepository.save(solicitudModificacion);
    }

    @Override
    public SolicitudModificacion buscarSolicitud(Long id){
        return this.solicitudesModRepository.findById(id).orElse(null);
    }

    @Override
    public List<SolicitudModificacion> buscarSolicitudesPendientes(){
        return this.solicitudesModRepository.findByEstadoSolicitud(EstadoHecho.PENDIENTE_DE_REVISION);
    }

    private Boolean estadoAceptado(Hecho hecho){

        return (hecho.getEstadoHecho() == EstadoHecho.ACEPTADA) || (hecho.getEstadoHecho() == EstadoHecho.ACEPTADA_CON_SUGERENCIA);

    }
}