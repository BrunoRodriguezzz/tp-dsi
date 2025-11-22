package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.clients.AgregadorClient;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorNotFound;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.SolicitudRevisadaInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudModOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Etiqueta;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.SolicitudModificacion;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IAdminService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminService implements IAdminService {

    private IRepositoryService dinamicaRepository;
    private final AgregadorClient agregadorClient;

    public AdminService(IRepositoryService dinamicaRepository, AgregadorClient agregadorClient) {
        this.dinamicaRepository = dinamicaRepository;
        this.agregadorClient = agregadorClient;
    }

    @Override
    public HechoOutputDTO gestionarHecho(HechoRevisadoInputDTO hechoRevisado){
        Hecho hecho = this.dinamicaRepository.buscarPorID(hechoRevisado.getId());

        if(hecho != null){
            List<Etiqueta> etiquetas = hechoRevisado
                    .getEtiquetas()
                    .stream()
                    .map(this::convertirEtiqueta)
                    .collect(Collectors.toList());

            hecho.setEtiquetas(etiquetas);
            hecho.setEstadoHecho(hechoRevisado.getEstadoHecho());
            hecho.setSugerenciaDeCambio(hechoRevisado.getSugerenciaDeCambio());

            this.dinamicaRepository.guardar(hecho);

            this.enviarHecho(hecho);

            return HechoOutputDTO.convertir(hecho);
        }else{
            throw new ErrorNotFound("No se encontro un hecho con el id " + hechoRevisado.getId() + ".");
        }
    }

    @Override
    public void eliminar(Long id) {

        Hecho hechoOriginal = this.dinamicaRepository.buscarPorID(id);

        if(hechoOriginal != null){
            hechoOriginal.setEstaEliminado(true);
            this.dinamicaRepository.guardar(hechoOriginal);
        }else{
            throw new ErrorNotFound("No se encontro un hecho con el id " + id + ".");
        }
    }

    @Override
    public List<HechoOutputDTO> obtenerHechosPendientes() {
        return this.dinamicaRepository
                .buscarPendientes()
                .stream()
                .map(HechoOutputDTO::convertir)
                .toList();
    }

    @Override
    public HechoOutputDTO obtenerHechoPendiente(Long id) {
        Hecho hecho = this.dinamicaRepository.buscarPendientesPorID(id);

        if(hecho == null) {
            throw new ErrorNotFound("No se encontro un hecho pendiente con el id " + id + ".");
        }

        return HechoOutputDTO.convertir(hecho);
    }

    @Override
    public List<SolicitudModOutputDTO> obtenerSolicitudesPendientes(){
        return this.dinamicaRepository.buscarSolicitudesPendientes()
                .stream()
                .map(SolicitudModOutputDTO::convertir)
                .collect(Collectors.toList());
    }

    @Override
    public SolicitudModOutputDTO aplicarCambios(SolicitudRevisadaInputDTO solicitud) {

        SolicitudModificacion solicitudGuardada = this.dinamicaRepository.buscarSolicitud(solicitud.getId());

        if(solicitud.getEstadoHecho() == EstadoHecho.ACEPTADA){

            Hecho hechoGuardado = this.dinamicaRepository.buscarPorID(solicitud.getIdHecho());


            hechoGuardado.setTitulo(solicitudGuardada.getTitulo());
            hechoGuardado.setDescripcion(solicitudGuardada.getDescripcion());
            hechoGuardado.setCategoria(solicitudGuardada.getCategoria());
            hechoGuardado.setContenidoMultimedia(new ArrayList<>(solicitudGuardada.getContenidoMultimedia()));
            hechoGuardado.setUbicacion(solicitudGuardada.getUbicacion());
            hechoGuardado.setFechaAcontecimiento(solicitudGuardada.getFechaAcontecimiento());
            hechoGuardado.setFechaModificacion(LocalDateTime.now());
            hechoGuardado.setFechaGuardado(LocalDateTime.now());

            solicitudGuardada.setEstadoSolicitud(EstadoHecho.ACEPTADA);
            this.dinamicaRepository.guardarSolicitudModificacion(solicitudGuardada);

            this.dinamicaRepository.guardar(hechoGuardado);

            this.enviarHechoMod(hechoGuardado);

            return SolicitudModOutputDTO.convertir(solicitudGuardada);

        }else{
            solicitudGuardada.setEstadoSolicitud(EstadoHecho.RECHAZADA);
            this.dinamicaRepository.guardarSolicitudModificacion(solicitudGuardada);

            return SolicitudModOutputDTO.convertir(solicitudGuardada);
        }
    }

    private void enviarHecho(Hecho hecho){ // TODO
        HechoOutputDTO hechoParaEnviar = HechoOutputDTO.convertir(hecho);

        try {
            agregadorClient.incorporarHecho(hechoParaEnviar);
            hecho.setEnviado(true);
            this.dinamicaRepository.guardar(hecho);

        } catch (Exception e){
            log.error("Fallo el envio del Hecho por un error inesperado: {}", e.getMessage());
        }
    }

    private Etiqueta convertirEtiqueta(String etiqueta){
        return Etiqueta
                .builder()
                .titulo(etiqueta)
                .build();
    }

    private void enviarHechoMod(Hecho hecho){ // TODO
        HechoOutputDTO hechoParaEnviar = HechoOutputDTO.convertir(hecho);

        try {
            this.agregadorClient.actualizarHechoDinamica(hechoParaEnviar, hecho.getId());
            hecho.setEnviado(true);
            this.dinamicaRepository.guardar(hecho);

        } catch (Exception e){
            log.error("Fallo el envio del Hecho por un error inesperado: {}", e.getMessage());
        }
    }
}