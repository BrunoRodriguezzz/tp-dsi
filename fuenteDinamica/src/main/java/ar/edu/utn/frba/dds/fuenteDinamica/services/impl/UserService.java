package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IUserService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private IRepositoryService dinamicaRepository;
    private IContribuyenteRepository contribuyentesRepository;

    public UserService(IRepositoryService dinamicaRepository, IContribuyenteRepository contribuyentesRepository){
        this.dinamicaRepository = dinamicaRepository;
        this.contribuyentesRepository = contribuyentesRepository;
    }

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
                this.contribuyentesRepository.save(usuario);
            }

            return SolicitudOutputDTO.convertir(hecho);
    }

    @Override
    public SolicitudOutputDTO actualizar(HechoModificadoInputDTO hechoModificado){

        List<Hecho> hechos = this.dinamicaRepository.buscarTodos();

        Hecho hechoOriginal = hechos.stream()
                .filter(hecho -> hecho.getId().equals(hechoModificado.getId()))
                .findFirst()
                .orElse(null);

        hechoOriginal.setTitulo(hechoModificado.getTitulo());
        hechoOriginal.setDescripcion(hechoModificado.getDescripcion());
        hechoOriginal.setCategoria(hechoModificado.getCategoria());
        hechoOriginal.setContenidoMultimedia(hechoModificado.getContenidoMultimedia());
        hechoOriginal.setFechaModificacion(LocalDateTime.now());

        Ubicacion ubicacion = Ubicacion
                .builder()
                .latitud(hechoModificado.getLatitud())
                .longitud(hechoModificado.getLongitud())
                .build();

        hechoOriginal.setUbicacion(ubicacion);
        hechoOriginal.setFechaAcontecimiento(hechoModificado.getFechaAcontecimiento());
        hechoOriginal.setFechaModificacion(LocalDateTime.now());
        hechoOriginal.setEstadoHecho(EstadoHecho.PENDIENTE_DE_REVISION);
        hechoOriginal.setEnviado(false);

        this.dinamicaRepository.guardar(hechoOriginal);

        return SolicitudOutputDTO.convertir(hechoOriginal);

    }

    @Override
    public Boolean verificarUsuarioRegistrado(HechoModificadoInputDTO hechoParaActualizar){

        Contribuyente usuario = Contribuyente
                .builder()
                .nombre(hechoParaActualizar.getNombreUsuario())
                .apellido(hechoParaActualizar.getApellidoUsuario())
                .fechaNacimiento(hechoParaActualizar.getFechaNacimientoUsuario())
                .build();

        return this.comprobarUsuarioRegistrado(usuario);

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

        Hecho hechoGuardado = this.dinamicaRepository.buscarPorID(solicitudCambio.getId());

        LocalDateTime fechaHoy = LocalDateTime.now();
        long diferencia = ChronoUnit.DAYS.between(hechoGuardado.getFechaGuardado(),fechaHoy);

        return diferencia <= 7;
    }

    private Boolean comprobarUsuarioRegistrado(Contribuyente contribuyente){

        List<Contribuyente> contribuyentes = this.contribuyentesRepository.findAll();

        return contribuyentes
                .stream()
                .anyMatch(usuario ->
                        usuario.getNombre().equals(contribuyente.getNombre())
                                && usuario.getApellido().equals(contribuyente.getApellido())
                                && usuario.getFechaNacimiento().equals(contribuyente.getFechaNacimiento()));

    }
}