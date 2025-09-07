package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoEliminarInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Etiqueta;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IAdminService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {

    private IRepositoryService dinamicaRepository;

    public AdminService(IRepositoryService dinamicaRepository){
        this.dinamicaRepository = dinamicaRepository;
    }

    private final WebClient webClient = WebClient
            .builder()
            .baseUrl("http://localhost:8082")
            .build();

    @Override
    public SolicitudOutputDTO gestionarHecho(HechoRevisadoInputDTO hechoRevisado){

        Hecho hecho = this.dinamicaRepository.buscarPorID(hechoRevisado.getId());

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

        return SolicitudOutputDTO.convertir(hecho);
    }

    @Override
    public void eliminar(HechoEliminarInputDTO hechoAEliminar, Long id) {

        Hecho hechoOriginal = this.dinamicaRepository.buscarPorID(id);

        hechoOriginal.setEstaEliminado(true);

        this.dinamicaRepository.guardar(hechoOriginal);
    }

    private void enviarHecho(Hecho hecho){
        HechoOutputDTO hechoParaEnviar = HechoOutputDTO.convertir(hecho);

        this.webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/hechos").build())
                .bodyValue(hechoParaEnviar)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> {
                    hecho.setEnviado(true);
                    this.dinamicaRepository.guardar(hecho);
                })
                .doOnError(error -> {
                    System.err.println("Fallo el envio del Hecho");
                })
                .subscribe();
    }

    private Etiqueta convertirEtiqueta(String etiqueta){
        return Etiqueta
                .builder()
                .titulo(etiqueta)
                .build();
    }
}
