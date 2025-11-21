package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {

    private IRepositoryService dinamicaRepository;
    private final WebClient webClient;

    public AdminService(IRepositoryService dinamicaRepository, @Value("${servicio.agregador}") String urlAgregador) {
        this.dinamicaRepository = dinamicaRepository;
        this.webClient = WebClient
            .builder()
            .baseUrl(urlAgregador)
            .build();
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

            //TODO: En realidad deberia enviar a un endpoint de PUT en el agregador
            this.enviarHechoMod(hechoGuardado);

            return SolicitudModOutputDTO.convertir(solicitudGuardada);

        }else{
            solicitudGuardada.setEstadoSolicitud(EstadoHecho.RECHAZADA);
            this.dinamicaRepository.guardarSolicitudModificacion(solicitudGuardada);

            return SolicitudModOutputDTO.convertir(solicitudGuardada);
        }
    }

    private void enviarHecho(Hecho hecho){
        HechoOutputDTO hechoParaEnviar = HechoOutputDTO.convertir(hecho);

        try {
            this.webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/hechos").build())
                    .contentType(MediaType.APPLICATION_JSON)  // aseguramos Content-Type
                    .bodyValue(hechoParaEnviar)
                    .retrieve()
                    .toBodilessEntity()
                    .block();  // bloquea y espera la respuesta

            hecho.setEnviado(true);
            this.dinamicaRepository.guardar(hecho);

        } catch (WebClientResponseException e) {
            // Capturamos la excepción específica de WebClient y mostramos detalles valiosos
            System.err.println("Fallo el envio del Hecho. Status: " + e.getStatusCode() + ". Body: " + e.getResponseBodyAsString());
            e.printStackTrace();
        } catch (Exception e){
            System.err.println("Fallo el envio del Hecho por un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Etiqueta convertirEtiqueta(String etiqueta){
        return Etiqueta
                .builder()
                .titulo(etiqueta)
                .build();
    }

    private void enviarHechoMod(Hecho hecho){
        HechoOutputDTO hechoParaEnviar = HechoOutputDTO.convertir(hecho);

        try {
            this.webClient.put()
                    .uri(uriBuilder -> uriBuilder.path("/hechos/contribuyentes/" + hecho.getId()).build())
                    .contentType(MediaType.APPLICATION_JSON)  // aseguramos Content-Type
                    .bodyValue(hechoParaEnviar)
                    .retrieve()
                    .toBodilessEntity()
                    .block();  // bloquea y espera la respuesta

            hecho.setEnviado(true);
            this.dinamicaRepository.guardar(hecho);

        } catch (WebClientResponseException e) {
            // Capturamos la excepción específica de WebClient y mostramos detalles valiosos
            System.err.println("Fallo el envio del Hecho. Status: " + e.getStatusCode() + ". Body: " + e.getResponseBodyAsString());
            e.printStackTrace();
        } catch (Exception e){
            System.err.println("Fallo el envio del Hecho por un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}