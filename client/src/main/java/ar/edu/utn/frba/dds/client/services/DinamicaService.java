package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.client.dtos.SolicitudCambiosDTO;
import ar.edu.utn.frba.dds.client.dtos.SolicitudModOutputDTO;
import ar.edu.utn.frba.dds.client.dtos.UbicacionDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDinamicaDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DinamicaService {

    private final WebClient webCient;
    private final WebApiCallerService webApiCallerService;
    private final String dinamicaUrl;

    public DinamicaService(WebApiCallerService webApiCallerService,
                           @Value("${dinamica.service.url}") String dinamicaUrl){
        this.webCient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.dinamicaUrl = dinamicaUrl;
    }

    public List<HechoDTO> mostrarMisHechos(Long id){
        try{
            return this.webApiCallerService.getList(this.dinamicaUrl + "/hechos/user/" + id, HechoDTO.class);
        } catch (Exception e) {

            // Si no esta activa la fuente dinamica envio unos hechos mockeados

            List<HechoDTO> misHechos = new ArrayList<>();

            // Hecho 1

            List<String> etiquetasHecho1 = new ArrayList<>();

            etiquetasHecho1.add("trabajo");
            etiquetasHecho1.add("derecho");
            etiquetasHecho1.add("manifestación");
            etiquetasHecho1.add("sector público");

            HechoDTO hecho = HechoDTO.builder()
                    .id(1L)
                    .titulo("Manifestación por derechos laborales")
                    .descripcion("Gran manifestación en el centro de la ciudad exigiendo mejores condiciones laborales y aumento salarial para trabajadores del sector público.")
                    .categoria("Protesta Social")
                    .fechaAcontecimiento(LocalDate.of(2024,1,15).atStartOfDay())
                    .fechaCarga(LocalDate.of(2024,1,26).atStartOfDay())
                    .ubicacion(UbicacionDTO.builder().municipio("Plaza De Mayo").provincia("Buenos Aires").build())
                    .etiquetas(etiquetasHecho1)
                    .origen("Contribuyentes")
                    .fuente("Fuente Dinamica")
                    .build();
            misHechos.add(hecho);

            // Hecho 2

            List<String> etiquetasHecho2 = new ArrayList<>();

            etiquetasHecho2.add("seguridad");
            etiquetasHecho2.add("policía");
            etiquetasHecho2.add("operativo");
            etiquetasHecho2.add("prevención");

            HechoDTO hecho2 = HechoDTO.builder()
                    .id(2L)
                    .titulo("Operativo de seguridad en zona céntrica")
                    .descripcion("Operativo policial preventivo en respuesta a reportes de actividad delictiva en el área comercial.")
                    .categoria("Seguridad")
                    .fechaAcontecimiento(LocalDate.of(2024,1,25).atStartOfDay())
                    .fechaCarga(LocalDate.of(2024,1,26).atStartOfDay())
                    .ubicacion(UbicacionDTO.builder().municipio("Microcentro").provincia("Buenos Aires").build())
                    .etiquetas(etiquetasHecho2)
                    .origen("Contribuyentes")
                    .fuente("Fuente Dinamica")
                    .build();
            misHechos.add(hecho2);

            return misHechos;

        }
    }

    public HechoDTO buscarHechoId(Long idHecho){
        HechoDTO hecho = null;
        try{
            hecho = this.webApiCallerService.get(this.dinamicaUrl + "/hechos/" + idHecho, HechoDTO.class);
        }catch (Exception e) {
             hecho = HechoDTO.builder()
                    .id(1L)
                    .titulo("Manifestación por derechos laborales")
                    .descripcion("Gran manifestación en el centro de la ciudad exigiendo mejores condiciones laborales y aumento salarial para trabajadores del sector público.")
                    .categoria("Protesta Social")
                    .fechaAcontecimiento(LocalDate.of(2024,1,15).atStartOfDay())
                    .fechaCarga(LocalDate.of(2024,1,26).atStartOfDay())
                    .ubicacion(UbicacionDTO.builder().municipio("Plaza De Mayo").provincia("Buenos Aires").build())
                    .origen("Contribuyentes")
                    .fuente("Fuente Dinamica")
                    .build();
             log.error(e.getMessage());
        }
        return hecho;
    }

    public void enviarHecho(HechoInputDTO hecho){
        this.webApiCallerService.post(this.dinamicaUrl + "/solicitud", hecho, Void.class);
    }

    public void gestionarHecho(HechoRevisadoForm form) {
        HechoDinamicaDTO dto = HechoDinamicaDTO.builder()
                .idAdministrador(form.getIdAdministrador())
                .id(form.getId())
                .etiquetas(form.getEtiquetas() != null ? List.of(form.getEtiquetas().split(",")) : List.of())
                .estadoHecho(form.getEstadoHecho())
                .sugerenciaDeCambio(form.getSugerenciaDeCambio())
                .build();

        System.out.println("[LOG] Enviando a fuenteDinamica: " + dto);

        try {
            WebClient.create("http://localhost:8081")
                    .post()
                    .uri("/api/fuenteDinamica/gestion")
                    .bodyValue(dto)
                    .retrieve()
                    .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("[ERROR] Falló la comunicación con fuenteDinamica: " + e.getMessage());
        }
    }

    public List<HechoDTO> obtenerHechosPendientes() {
        try {
            return this.webApiCallerService.getList(this.dinamicaUrl + "/pendientes", HechoDTO.class);
        } catch (Exception e) {
            System.err.println("[ERROR] Falló la obtención de hechos pendientes desde fuenteDinamica: " + e.getMessage());
            // Mock de hechos pendientes si la fuente dinámica no responde
            List<HechoDTO> hechosPendientes = new ArrayList<>();

            List<String> etiquetas1 = List.of("pendiente", "social", "urgente");
            HechoDTO hecho1 = HechoDTO.builder()
                    .id(101L)
                    .titulo("Corte de calle por manifestación")
                    .descripcion("Manifestación espontánea que genera corte de tránsito en el centro.")
                    .categoria("Protesta Social")
                    .fechaAcontecimiento(LocalDate.of(2024,2,10).atStartOfDay())
                    .fechaCarga(LocalDate.of(2024,2,11).atStartOfDay())
                    .ubicacion(UbicacionDTO.builder().municipio("Centro").provincia("Buenos Aires").build())
                    .etiquetas(etiquetas1)
                    .origen("Contribuyentes")
                    .fuente("Fuente Dinamica")
                    .build();
            hechosPendientes.add(hecho1);

            List<String> etiquetas2 = List.of("pendiente", "seguridad", "relevante");
            HechoDTO hecho2 = HechoDTO.builder()
                    .id(102L)
                    .titulo("Operativo policial pendiente de aprobación")
                    .descripcion("Operativo policial en revisión por el área de seguridad.")
                    .categoria("Seguridad")
                    .fechaAcontecimiento(LocalDate.of(2024,2,12).atStartOfDay())
                    .fechaCarga(LocalDate.of(2024,2,13).atStartOfDay())
                    .ubicacion(UbicacionDTO.builder().municipio("Microcentro").provincia("Buenos Aires").build())
                    .etiquetas(etiquetas2)
                    .origen("Contribuyentes")
                    .fuente("Fuente Dinamica")
                    .build();
            hechosPendientes.add(hecho2);

            return hechosPendientes;
        }
    }

    public HechoDTO buscarPendienteID(Long id) {
        try {
            return this.webApiCallerService.get(this.dinamicaUrl + "/pendientes/" + id, HechoDTO.class);
        }
        catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
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
        return this.webApiCallerService.getList(this.dinamicaUrl + "/modificacion", SolicitudModOutputDTO.class);
    }

    public List<HechoDTO> obtenerHechos() {
        return this.webApiCallerService.getList(this.dinamicaUrl + "/hechos", HechoDTO.class);
    }

    public List<SolicitudCambiosDTO> obtenerSolicitudesCambios() {
        List<SolicitudModOutputDTO> solicitudes = this.obtenerSolicitudesMod();
        List<HechoDTO> hechos = this.obtenerHechos();

        Map<Long, HechoDTO> hechosPorId = hechos.stream()
                .collect(Collectors.toMap(HechoDTO::getId, h -> h));

        List<SolicitudCambiosDTO> comparativas = solicitudes.stream()
                .map(sol -> new SolicitudCambiosDTO(sol, hechosPorId.get(sol.getIdHecho())))
                .toList();

        return comparativas;
    }

    public void modificar(HechoRevisadoForm hechoDTO) {
        try {

            log.info("Modificando el hecho con id {}", hechoDTO.getId());
            log.info("Modificando el hecho con id {}", hechoDTO.getIdHecho());
            log.info("Modificando el hecho con id {}", hechoDTO.getEtiquetas());
            log.info("Modificando el hecho con id {}", hechoDTO.getEstadoHecho());



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
