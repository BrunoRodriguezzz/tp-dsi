package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoEliminarInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IAdminService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

        Hecho hechoActual = this.dinamicaRepository.buscarPorID(hechoRevisado.getId());

        Hecho hechoCambiado = this.dinamicaRepository.buscarPorID(hechoRevisado.getId());

        hechoCambiado.setEtiquetas(hechoRevisado.getEtiquetas());
        hechoCambiado.setEstadoHecho(hechoRevisado.getEstadoHecho());
        hechoCambiado.setSugerenciaDeCambio(hechoRevisado.getSugerenciaDeCambio());

        this.dinamicaRepository.guardarCambios(hechoActual,hechoCambiado);

        this.enviarHecho(hechoCambiado);

        return SolicitudOutputDTO.convertir(hechoCambiado);
    }

    @Override
    public void eliminar(HechoEliminarInputDTO hechoAEliminar, Long id) {

        Hecho hechoOriginal = this.dinamicaRepository.buscarPorID(id);

        if(this.sonIguales(hechoAEliminar,hechoOriginal)){

            Hecho hechoAGuardar = this.dinamicaRepository.buscarPorID(id);
            hechoAGuardar.setEstaEliminado(true);

            this.dinamicaRepository.guardarCambios(hechoOriginal,hechoAGuardar);
        }
    }

    private void enviarHecho(Hecho hecho){
        HechoOutputDTO hechoParaEnviar = HechoOutputDTO.convertir(hecho);

        Hecho hechoAntiguo = this.dinamicaRepository.buscarPorID(hecho.getId());

        hechoAntiguo.setEnviado(true);

        this.dinamicaRepository.guardarCambios(hecho,hechoAntiguo);

        this.webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/hechos").build())
                .bodyValue(hechoParaEnviar)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    private Boolean sonIguales(HechoEliminarInputDTO hechoA, Hecho hechoB){

        return hechoA.getContribuyente().getNombre().equals(hechoB.getContribuyente().getNombre())
                && hechoA.getContribuyente().getApellido().equals(hechoB.getContribuyente().getApellido())
                && hechoA.getContribuyente().getFechaNacimiento().equals(hechoB.getContribuyente().getFechaNacimiento())
                && hechoA.getId().equals(hechoB.getId())
                && hechoA.getCategoria().equals(hechoB.getCategoria())
                && hechoA.getTitulo().equals(hechoB.getTitulo())
                && hechoA.getDescripcion().equals(hechoB.getDescripcion())
                && hechoA.getFechaAcontecimiento().equals(hechoB.getFechaAcontecimiento())
                && hechoA.getUbicacion().getLatitud().equals(hechoB.getUbicacion().getLatitud())
                && hechoA.getUbicacion().getLongitud().equals(hechoB.getUbicacion().getLongitud());
    }
}
