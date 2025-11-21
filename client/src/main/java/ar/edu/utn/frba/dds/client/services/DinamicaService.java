package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.*;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDinamicaDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DinamicaService {

    private final WebApiCallerService webApiCallerService;
    private final String dinamicaUrl;

    public DinamicaService(WebApiCallerService webApiCallerService,
                           @Value("${servicio.apiGateway}") String gatewayURL){
        this.webApiCallerService = webApiCallerService;
        this.dinamicaUrl = gatewayURL + "/api/fuenteDinamica";
    }

    public List<HechoDTO> mostrarMisHechos(Long id){
        try{
            return this.webApiCallerService.getList(this.dinamicaUrl + "/hechos/user/" + id, HechoDTO.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public HechoDTO buscarHechoId(Long idHecho){
        try{
            return this.webApiCallerService.get(this.dinamicaUrl + "/hechos/" + idHecho, HechoDTO.class);
        }catch (Exception e) {
             log.error(e.getMessage());
             return null;
        }
    }

    public void enviarHecho(HechoInputDTO hecho){
        try {
            log.info("Tratando de enviar el hecho...");
            WebClient.builder()
                    .baseUrl(dinamicaUrl)
                    .build()
                    .post()
                    .uri("/solicitud")
                    .bodyValue(hecho)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class).map(RuntimeException::new)
                    )
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            log.error("Error al enviar el hecho", e);
        }
    }

    public void gestionarHecho(HechoRevisadoForm form) {
        if(form.getEtiquetas().isEmpty()) form.setEtiquetas(null);

        HechoDinamicaDTO dto = HechoDinamicaDTO.builder()
                .idAdministrador(form.getIdAdministrador())
                .id(form.getId())
                .etiquetas(form.getEtiquetas() != null ? List.of(form.getEtiquetas().split(",")) : List.of())
                .estadoHecho(form.getEstadoHecho())
                .sugerenciaDeCambio(form.getSugerenciaDeCambio())
                .build();

        try {
            WebClient.create(dinamicaUrl)
                    .post()
                    .uri("/gestion")
                    .bodyValue(dto)
                    .retrieve()
                    .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al gestionar el hecho", e);
        }
    }

    public List<HechoDTO> obtenerHechosPendientes() {
        try {
            return this.webApiCallerService.getList(this.dinamicaUrl + "/pendientes", HechoDTO.class);
        } catch (Exception e) {
            log.error("Falló la obtención de hechos pendientes desde fuenteDinamica: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public HechoDTO buscarPendienteID(Long id) {
        try {
            return this.webApiCallerService.get(this.dinamicaUrl + "/pendientes/" + id, HechoDTO.class);
        }
        catch (Exception e) {
            log.error("Error buscando el hecho pendiente de ID: {}, Error: {}", id, e.getMessage());
            return null;
        }
    }

    public boolean modificarHecho(HechoDTO hechoDTO) {
        try {
            SolicitudModOutputDTO respuesta = WebClient.builder().baseUrl(dinamicaUrl)
                    .build()
                    .patch()
                    .uri("/modificacion")
                    .bodyValue(hechoDTO)
                    .retrieve()
                    .onStatus(
                            httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new)
                    )
                    .bodyToMono(SolicitudModOutputDTO.class)
                    .block();
            return respuesta != null && respuesta.getIdSolicitud() != null;
        } catch (Exception e) {
            log.error("Error al modificar el hecho con id {}: {}", hechoDTO.getId(), e.getMessage());
            return false;
        }
    }

    public List<SolicitudModOutputDTO> obtenerSolicitudesMod() {
        try {
            return this.webApiCallerService.getList(this.dinamicaUrl + "/modificacion", SolicitudModOutputDTO.class);
        } catch (Exception e) {
            log.error("Falló la obtención de solicitudes de modificación desde fuenteDinamica: {}", e.getMessage());
            return new ArrayList<>();
        }

    }

    public List<HechoDTO> obtenerHechos() {
        try {
            return this.webApiCallerService.getList(this.dinamicaUrl + "/hechos", HechoDTO.class);
        } catch (Exception e) {
            log.error("Falló la obtención de hechos desde fuenteDinamica: {}", e.getMessage());
            return new ArrayList<>();
        }

    }

    public List<SolicitudCambiosDTO> obtenerSolicitudesCambios() {
        List<SolicitudModOutputDTO> solicitudes = this.obtenerSolicitudesMod();
        List<HechoDTO> hechos = this.obtenerHechos();

        Map<Long, HechoDTO> hechosPorId = hechos.stream()
                .collect(Collectors.toMap(HechoDTO::getId, h -> h));

        return solicitudes.stream()
                .map(sol -> new SolicitudCambiosDTO(sol, hechosPorId.get(sol.getIdHecho())))
                .toList();
    }

    public void modificar(HechoRevisadoForm hechoDTO) {
        try {
            WebClient.builder().baseUrl(dinamicaUrl)
                    .build()
                    .put()
                    .uri("/modificacion")
                    .bodyValue(hechoDTO)
                    .retrieve()
                    .onStatus(
                            httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new)
                    )
                    .bodyToMono(SolicitudModOutputDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al modificar el hecho con id {}: {}", hechoDTO.getId(), e.getMessage());
        }
    }
}
