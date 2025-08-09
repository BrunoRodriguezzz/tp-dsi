package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoEliminarInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IUserService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UserService implements IUserService {

    private IRepositoryService dinamicaRepository;

    @Autowired
    private IContribuyenteRepository contribuyentesRepository;

    @Override
    public SolicitudOutputDTO crear(HechoInputDTO hechoInputDTO) {
            Contribuyente usuario = Contribuyente
                    .builder()
                    .nombre(hechoInputDTO.getNombreUsuario())
                    .apellido(hechoInputDTO.getApellidoUsuario())
                    .fechaNacimiento(hechoInputDTO.getFechaNacimientoUsuario())
                    .build();

            Ubicacion ubicacion = Ubicacion
                    .builder()
                    .latitud(hechoInputDTO.getLatitud())
                    .longitud(hechoInputDTO.getLongitud())
                    .build();

            Hecho hecho = Hecho
                    .builder()
                    .titulo(hechoInputDTO.getTitulo())
                    .descripcion(hechoInputDTO.getDescripcion())
                    .categoria(hechoInputDTO.getCategoria())
                    .contenidoMultimedia(hechoInputDTO.getContenidoMultimedia())
                    .ubicacion(ubicacion)
                    .fechaAcontecimiento(hechoInputDTO.getFechaAcontecimiento())
                    .contribuyente(usuario)
                    .origen("CONTRIBUYENTE")
                    .fuente("Provistos por contribuyentes")
                    .build();

            this.dinamicaRepository.guardar(hecho);

            // Guardo el contribuyente, solo si indico su nombre
            if(usuario.getNombre() != "" && usuario.getApellido() != ""){
                this.contribuyentesRepository.guardar(usuario);
            }

            return SolicitudOutputDTO.convertir(hecho);
    }

    @Override
    public SolicitudOutputDTO actualizar(HechoModificadoInputDTO hechoModificado){

        Hecho hechoOriginal = this.dinamicaRepository.buscarPorID(hechoModificado.getId());

        Hecho hechoCambiado = this.dinamicaRepository.buscarPorID(hechoModificado.getId());

        hechoCambiado.setTitulo(hechoModificado.getTitulo());
        hechoCambiado.setDescripcion(hechoModificado.getDescripcion());
        hechoCambiado.setCategoria(hechoModificado.getCategoria());
        hechoCambiado.setContenidoMultimedia(hechoModificado.getContenidoMultimedia());
        hechoCambiado.setFechaModificacion(LocalDateTime.now());

        Ubicacion ubicacion = Ubicacion
                .builder()
                .latitud(hechoModificado.getLatitud())
                .longitud(hechoModificado.getLongitud())
                .build();

        hechoCambiado.setUbicacion(ubicacion);
        hechoCambiado.setFechaAcontecimiento(hechoModificado.getFechaAcontecimiento());
        hechoCambiado.setFechaModificacion(LocalDateTime.now());
        hechoCambiado.setEstadoHecho(EstadoHecho.PENDIENTE_DE_REVISION);
        hechoCambiado.setEnviado(false);

        this.dinamicaRepository.guardarCambios(hechoOriginal,hechoCambiado);

        return SolicitudOutputDTO.convertir(hechoCambiado);

    }

    @Override
    public Boolean verificarUsuarioRegistrado(HechoModificadoInputDTO hechoParaActualizar){

        Contribuyente usuario = Contribuyente
                .builder()
                .nombre(hechoParaActualizar.getNombreUsuario())
                .apellido(hechoParaActualizar.getApellidoUsuario())
                .fechaNacimiento(hechoParaActualizar.getFechaNacimientoUsuario())
                .build();

        return this.contribuyentesRepository.comprobarUsuarioRegistrado(usuario);

    }

    @Override
    public Boolean verificarEdadNecesaria(HechoInputDTO solicitud){

        LocalDate fechaHoy = LocalDate.now();
        Long diferencia = ChronoUnit.YEARS.between(solicitud.getFechaNacimientoUsuario(),fechaHoy);

        return diferencia>18;
    }

    @Override
    public Boolean verificarTiposDeDatos(HechoInputDTO hechoIngresado){
        return hechoIngresado.getNombreUsuario() != null
                && hechoIngresado.getApellidoUsuario() != null
                && hechoIngresado.getFechaNacimientoUsuario() != null
                && hechoIngresado.getTitulo() != null
                && hechoIngresado.getDescripcion() != null
                && hechoIngresado.getCategoria() != null
                && hechoIngresado.getContenidoMultimedia() != null
                && hechoIngresado.getLatitud() != null
                && hechoIngresado.getLongitud() != null
                && hechoIngresado.getFechaAcontecimiento() != null;
    }

    @Override
    public String tipoDeDatoErroneo(HechoInputDTO solicitudHecho){

        String datoErroneo = "";

        if(solicitudHecho.getNombreUsuario() == null)
            datoErroneo = "Nombre de Usuario";
        if(solicitudHecho.getApellidoUsuario() == null)
            datoErroneo = "Apellido de Usuario";
        if(solicitudHecho.getFechaNacimientoUsuario() == null)
            datoErroneo = "Fecha de Nacimiento";
        if(solicitudHecho.getTitulo() == null || solicitudHecho.getTitulo().isEmpty())
            datoErroneo = "Titulo";
        if(solicitudHecho.getDescripcion() == null || solicitudHecho.getDescripcion().isEmpty())
            datoErroneo = "Descripcion";
        if(solicitudHecho.getCategoria() == null || solicitudHecho.getCategoria().isEmpty())
            datoErroneo = "Categoria";
        if(solicitudHecho.getContenidoMultimedia() == null)
            datoErroneo = "Contenido Multimedia";
        if(solicitudHecho.getLatitud() == null)
            datoErroneo = "Latitud";
        if(solicitudHecho.getLongitud() == null)
            datoErroneo = "Longitud";
        if(solicitudHecho.getFechaAcontecimiento() == null)
            datoErroneo = "Fecha de Acontecimiento";

        return datoErroneo;
    }

    @Override
    public Boolean verificarTiempoParaActualizar(HechoModificadoInputDTO solicitudCambio){

        Hecho hechoGuardado = this.buscarPorID(solicitudCambio.getId());

        LocalDateTime fechaHoy = LocalDateTime.now();
        long diferencia = ChronoUnit.DAYS.between(hechoGuardado.getFechaGuardado(),fechaHoy);

        return diferencia <= 7;
    }

    private Hecho buscarPorID(Long id) {

        return this.dinamicaRepository.buscarPorID(id);

    }
}