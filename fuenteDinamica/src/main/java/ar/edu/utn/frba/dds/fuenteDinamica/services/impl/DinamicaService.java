package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IDinamicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DinamicaService implements IDinamicaService {

    @Autowired
    private IDinamicaRepository dinamicaRepository;

    @Autowired
    private IContribuyenteRepository contribuyentesRepository;

    @Override
    public List<HechoOutputDTO> buscarHechos(Boolean enviado) {
        if(enviado != null){
            return dinamicaRepository
                    .mostrarEnviados(enviado)
                    .stream()
                    .map(this::hechoOutputDTO)
                    .toList();
        }

        return this.dinamicaRepository
                .mostrarTodos()
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO crear(HechoInputDTO hechoInputDTO) {

        Hecho hecho = new Hecho();

        hecho.setTitulo(hechoInputDTO.getTitulo());
        hecho.setDescripcion(hechoInputDTO.getDescripcion());
        hecho.setCategoria(hechoInputDTO.getCategoria());
        hecho.setUrlMultimedia(hechoInputDTO.getUrlMultimedia());
        hecho.setUbicacion((hechoInputDTO.getUbicacion()));
        hecho.setFechaAcontecimiento(hechoInputDTO.getFechaAcontecimiento());

        Contribuyente usuario = new Contribuyente();

        usuario.setNombre(hechoInputDTO.getNombreUsuario());
        usuario.setEdad(hechoInputDTO.getEdadUsuario());

        hecho.setContribuyente(usuario);

        this.dinamicaRepository.guardar(hecho);

        // Guardo el contribuyente, solo si indico su nombre
        if(usuario.getNombre() != ""){
            this.contribuyentesRepository.guardar(usuario);
        }

        return this.hechoOutputDTO(hecho);
    }

    @Override
    public HechoOutputDTO actualizar(HechoModificadoInputDTO hechoModificado){

        Hecho hechoOriginal = this.dinamicaRepository.buscarPorID(hechoModificado.getId());

        if(this.verificarTiempoParaActualizar(hechoOriginal)){

            Hecho hechoCambiado = this.dinamicaRepository.buscarPorID(hechoModificado.getId());

            hechoCambiado.setTitulo(hechoModificado.getTitulo());
            hechoCambiado.setDescripcion(hechoModificado.getDescripcion());
            hechoCambiado.setCategoria(hechoModificado.getCategoria());
            hechoCambiado.setUrlMultimedia(hechoModificado.getUrlMultimedia());
            hechoCambiado.setUbicacion(hechoModificado.getUbicacion());
            hechoCambiado.setFechaAcontecimiento(hechoModificado.getFechaAcontecimiento());
            hechoCambiado.setEstadoHecho(EstadoHecho.PENDIENTE_DE_REVISION);
            hechoCambiado.setEnviado(false);

            this.dinamicaRepository.guardarCambios(hechoOriginal,hechoCambiado);

            return this.hechoOutputDTO(hechoCambiado);
        }else{
            //TODO: El tiempo para actualizar ya se vencio
            return null;
        }
    }

    @Override
    public void eliminar(Long id) {
        // TODO: Revisar enunciado, ¿puede eliminarse por solicitud o decision del Administrador?
    }

    @Override
    public Boolean verificarUsuarioRegistrado(Contribuyente contribuyente){

        return this.contribuyentesRepository.comprobarUsuarioRegistrado(contribuyente);

    }

    @Override
    public HechoOutputDTO gestionarHecho(HechoRevisadoInputDTO hechoRevisado){

        Hecho hechoActual = this.buscarPorID(hechoRevisado.getIdHecho());

        Hecho hechoCambiado = this.buscarPorID(hechoRevisado.getIdHecho());

        hechoCambiado.setEtiquetas(hechoRevisado.getEtiquetas());
        hechoCambiado.setEstadoHecho(hechoRevisado.getEstadoHecho());
        hechoCambiado.setSugerenciaDeCambio(hechoRevisado.getSugerenciaDeCambio());

        this.dinamicaRepository.guardarCambios(hechoActual,hechoCambiado);

        return this.hechoOutputDTO(hechoCambiado);
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho){

        HechoOutputDTO hechoOutputDTO = new HechoOutputDTO();

        hechoOutputDTO.setIdHecho(hecho.getIdHecho());
        hechoOutputDTO.setContribuyente(hecho.getContribuyente());
        hechoOutputDTO.setTitulo(hecho.getTitulo());
        hechoOutputDTO.setDescripcion(hecho.getDescripcion());
        hechoOutputDTO.setCategoria(hecho.getCategoria());
        hechoOutputDTO.setUrlMultimedia(hecho.getUrlMultimedia());
        hechoOutputDTO.setUbicacion(hecho.getUbicacion());
        hechoOutputDTO.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        hechoOutputDTO.setEtiquetas(hecho.getEtiquetas());
        hechoOutputDTO.setEstadoHecho(hecho.getEstadoHecho());
        hechoOutputDTO.setSugerenciaDeCambio(hecho.getSugerenciaDeCambio());

        return hechoOutputDTO;
    }

    private Hecho buscarPorID(Long id) {
        return this.dinamicaRepository.buscarPorID(id);
    }

    private Boolean verificarTiempoParaActualizar(Hecho hecho){

        LocalDate fechaHoy = LocalDate.now();
        Period diferencia = Period.between(hecho.getFechaGuardado(),fechaHoy);

        if(diferencia.getDays() > 7){
            //TODO: No se puede actualizar el hecho ya paso una semana
            return false;
        }else{
            return true;
        }
    }
}