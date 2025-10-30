package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoNoAutorizado;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoProhibido;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorDeTiempo;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudModOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.*;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IUbicacionRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IUserService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IRepositoryService;
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

    private final IRepositoryService dinamicaRepository;
    private final IContribuyenteRepository contribuyentesRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IUbicacionRepository ubicacionRepository;


    public UserService(IRepositoryService dinamicaRepository,
                       IContribuyenteRepository contribuyentesRepository,
                       ICategoriaRepository categoriaRepository,
                       IUbicacionRepository ubicacionRepository){

        this.dinamicaRepository = dinamicaRepository;
        this.contribuyentesRepository = contribuyentesRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;

    }

    @Override
    public SolicitudOutputDTO crear(HechoInputDTO hechoInputDTO) {

        if(this.verificarEdadNecesaria(hechoInputDTO)){

            Usuario usuario = Usuario
                    .builder()
                    .nombre(hechoInputDTO.getNombreUsuario())
                    .apellido(hechoInputDTO.getApellidoUsuario())
                    .fechaNacimiento(hechoInputDTO.getFechaNacimientoUsuario())
                    .idUsuario(hechoInputDTO.getIdUsuario())
                    .build();


            List<ContenidoMultimedia> contenidoMultimedia = hechoInputDTO
                    .getContenidoMultimedia()
                    .stream()
                    .map(this::convertirMultimedia)
                    .toList();

            Categoria categoria = categoriaExistente(hechoInputDTO.getCategoria());
            Ubicacion ubicacion = ubicacionExistente(hechoInputDTO.getLatitud(), hechoInputDTO.getLongitud());

            Hecho hecho = Hecho
                    .builder()
                    .titulo(hechoInputDTO.getTitulo())
                    .descripcion(hechoInputDTO.getDescripcion())
                    .categoria(categoria)
                    .ubicacion(ubicacion)
                    .contenidoMultimedia(contenidoMultimedia)
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
                            .fechaNacimiento(usuario.getFechaNacimiento().atStartOfDay())
                            .idUsuario(usuario.getIdUsuario())
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
    public SolicitudModOutputDTO actualizar(HechoModificadoInputDTO hechoModificado){

        if(this.verificarUsuarioRegistrado(hechoModificado)){
            if(this.verificarTiempoParaActualizar(hechoModificado)){

                List<ContenidoMultimedia> contenido = hechoModificado
                        .getContenidoMultimedia()
                        .stream()
                        .map(this::convertirMultimedia)
                        .collect(Collectors.toCollection(ArrayList::new));

                Categoria categoria = this.categoriaExistente(hechoModificado.getCategoria());

                Ubicacion ubicacion = this.ubicacionExistente(hechoModificado.getLatitud(),  hechoModificado.getLongitud());

                SolicitudModificacion nuevaSolicitud = SolicitudModificacion
                        .builder()
                        .idHecho(hechoModificado.getId())
                        .titulo(hechoModificado.getTitulo())
                        .descripcion(hechoModificado.getDescripcion())
                        .categoria(categoria)
                        .ubicacion(ubicacion)
                        .contenidoMultimedia(contenido)
                        .fechaAcontecimiento(hechoModificado.getFechaAcontecimiento())
                        .estadoSolicitud(EstadoHecho.PENDIENTE_DE_REVISION)
                        .build();

                this.dinamicaRepository.guardarSolicitudModificacion(nuevaSolicitud);

                return SolicitudModOutputDTO.convertir(nuevaSolicitud);
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
                .idUsuario(hechoParaActualizar.getIdUsuario())
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

        Optional<Contribuyente> contribuyentes = this.contribuyentesRepository.findById(contribuyente.getIdUsuario());

        if(contribuyentes.isEmpty()){
            return false;
        }
        return true;
    }

    private ContenidoMultimedia convertirMultimedia(String url){

        return ContenidoMultimedia.builder().url(url).build();
    }

    private Categoria categoriaExistente(String categoria){
        Categoria categoriaGuardada = this.categoriaRepository.findByNombre(categoria);

        if(categoriaGuardada==null){
            Categoria categoriaNueva = this.convertirCategoria(categoria);
            this.categoriaRepository.save(categoriaNueva);
            return categoriaNueva;
        }else{
            return categoriaGuardada;
        }
    }

    private Categoria convertirCategoria(String categoria){
        return Categoria.builder().nombre(categoria).build();
    }

    private Ubicacion ubicacionExistente(String latitud,String longitud){
        Ubicacion ubicacionGuardada = this.ubicacionRepository.findByLatitudAndLongitud(latitud,longitud);

        if(ubicacionGuardada==null){

            Ubicacion nuevaUbicacion = Ubicacion
                    .builder()
                    .latitud(latitud)
                    .longitud(longitud)
                    .pais("ARGENTINA")
                    .build();

            this.ubicacionRepository.save(nuevaUbicacion);

            return nuevaUbicacion;

        }else{
            return ubicacionGuardada;
        }
    }

}