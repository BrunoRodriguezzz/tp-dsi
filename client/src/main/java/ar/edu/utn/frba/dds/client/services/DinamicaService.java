package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.UbicacionDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

            List<HechoDTO> misHechos = this.webApiCallerService.getList(this.dinamicaUrl + "/hechos/" + id, HechoDTO.class);

            return misHechos;

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
        }
        return hecho;
    }

    public void enviarHecho(HechoInputDTO hecho){
        this.webApiCallerService.post(this.dinamicaUrl + "/solicitud", hecho, HechoDTO.class);
    }
}
