package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoNoAutorizado;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoProhibido;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorDeTiempo;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.*;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IUserService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if(this.verificarEdadNecesaria(hechoInputDTO)){

            Usuario usuario = Usuario
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

            List<ContenidoMultimedia> contenidoMultimedia = hechoInputDTO
                    .getContenidoMultimedia()
                    .stream()
                    .map(this::convertirMultimedia)
                    .toList();

            Categoria categoria = this.convertirCategoria(hechoInputDTO.getCategoria());

            Hecho hecho = Hecho
                    .builder()
                    .titulo(hechoInputDTO.getTitulo())
                    .descripcion(hechoInputDTO.getDescripcion())
                    .categoria(categoria)
                    .contenidoMultimedia(contenidoMultimedia)
                    .ubicacion(ubicacion)
                    .fechaGuardado(LocalDateTime.now())
                    .estaEliminado(false)
                    .enviado(false)
                    .fechaAcontecimiento(hechoInputDTO.getFechaAcontecimiento())
                    .estadoHecho(EstadoHecho.PENDIENTE_DE_REVISION)
                    .origen("CONTRIBUYENTE")
                    .fuente("Provistos por contribuyentes")
                    .build();


            // Guardo el contribuyente, solo si indico su nombre
            if(usuario.getNombre() != "" && usuario.getApellido() != ""){
                if(!this.comprobarUsuarioRegistrado(usuario)) {

                    // Si no esta registrado se instancia el Contribuyente

                    Contribuyente nuevoContribuyente = Contribuyente.builder()
                            .nombre(usuario.getNombre())
                            .apellido(usuario.getApellido())
                            .fechaNacimiento(usuario.getFechaNacimiento())
                            .build();

                    hecho.setContribuyente(nuevoContribuyente);

                    this.contribuyentesRepository.save(nuevoContribuyente);
                }else{

                    // Si esta registrado se le asigna el hecho

                    List<Contribuyente> contribuyentes = this.contribuyentesRepository.findAll();

                    Contribuyente contribuyenteRegistrado = contribuyentes
                            .stream()
                            .filter(contribuyente -> this.comprobarUsuarioRegistrado(usuario))
                            .findFirst()
                            .orElse(null);

                    hecho.setContribuyente(contribuyenteRegistrado);
                }
            }

            this.dinamicaRepository.guardar(hecho);

            return SolicitudOutputDTO.convertir(hecho);
            
        }else{
            throw new ErrorAccesoProhibido("No cumple con la mayoria de edad.");
        }
    }

    @Override
    public SolicitudOutputDTO actualizar(HechoModificadoInputDTO hechoModificado){

        if(this.verificarUsuarioRegistrado(hechoModificado)){
            if(this.verificarTiempoParaActualizar(hechoModificado)){
                List<Hecho> hechos = this.dinamicaRepository.buscarTodos();

                Hecho hechoOriginal = hechos
                        .stream()
                        .filter(hecho -> hecho.getId().equals(hechoModificado.getId()))
                        .findFirst()
                        .orElse(null);

                Categoria categoria = this.convertirCategoria(hechoModificado.getCategoria());

                List<ContenidoMultimedia> contenido = hechoModificado
                        .getContenidoMultimedia()
                        .stream()
                        .map(this::convertirMultimedia)
                        .collect(Collectors.toCollection(ArrayList::new));

                hechoOriginal.setTitulo(hechoModificado.getTitulo());
                hechoOriginal.setDescripcion(hechoModificado.getDescripcion());
                hechoOriginal.setCategoria(categoria);
                hechoOriginal.setContenidoMultimedia(contenido);
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
            }else{
                throw new ErrorDeTiempo("El plazo para modificar el hecho se ha terminado y no es posible modificar el hecho.");
            }
        }else{
            throw new ErrorAccesoNoAutorizado("No existe un usuario registrado con estos datos.");
        }
    }

    @Override
    public Boolean verificarUsuarioRegistrado(HechoModificadoInputDTO hechoParaActualizar){

        Usuario usuario = Usuario
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
    public Boolean verificarTiempoParaActualizar(HechoModificadoInputDTO solicitudCambio){

        Hecho hechoGuardado = this.dinamicaRepository.buscarPorID(solicitudCambio.getId());

        LocalDateTime fechaHoy = LocalDateTime.now();
        long diferencia = ChronoUnit.DAYS.between(hechoGuardado.getFechaGuardado(),fechaHoy);

        return diferencia <= 7;
    }

    private Boolean comprobarUsuarioRegistrado(Usuario contribuyente){

        List<Contribuyente> contribuyentes = this.contribuyentesRepository.findAll();

        return contribuyentes
                .stream()
                .anyMatch(usuario ->
                        usuario.getNombre().equals(contribuyente.getNombre())
                                && usuario.getApellido().equals(contribuyente.getApellido())
                                && usuario.getFechaNacimiento().equals(contribuyente.getFechaNacimiento()));

    }

    private ContenidoMultimedia convertirMultimedia(String url){

        return ContenidoMultimedia.builder().url(url).build();
    }

    private Categoria convertirCategoria(String categoria){
        return Categoria.builder().nombre(categoria).build();
    }
}