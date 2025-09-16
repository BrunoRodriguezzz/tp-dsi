package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoNoAutorizado;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoProhibido;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorDeTiempo;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorTipoDeDatos;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fuenteDinamica")
@CrossOrigin(origins = "http://localhost:8081")
public class UserController {

    @Autowired
    private IUserService userService;

    // Solicitud de crear un hecho

    @PostMapping("/solicitud")
    public SolicitudOutputDTO solicitudCrearHecho(@RequestBody HechoInputDTO hecho){

        if(verificarTiposDeDatos(hecho)) {
            return userService.crear(hecho);
        }else{
            throw new ErrorTipoDeDatos("Error de ingreso de datos en: " + tipoDeDatoErroneo(hecho) + ". No puede haber campos vacios.");
        }

    }

    // Solicitud de modificar un hecho

    @PatchMapping("/modificacion")
    public SolicitudOutputDTO actualizarHecho(@RequestBody HechoModificadoInputDTO hecho){
        if(verificarUsuarioRegistrado(hecho)){
            if(verificarTiempoParaActualizar(hecho)){
                return userService.actualizar(hecho);
            }else{
                throw new ErrorDeTiempo("El plazo para modificar el hecho se termino.");
            }
        }else{
            throw new ErrorAccesoNoAutorizado("Usuario no registrado.");
        }

        //TODO: Estas validaciones correspondan a la capa de Service, no a la de Controllers
    }

    // Verificadores necesarios

    private Boolean verificarUsuarioRegistrado(HechoModificadoInputDTO hechoParaActualizar){

        return this.userService.verificarUsuarioRegistrado(hechoParaActualizar);
    }

    private Boolean verificarTiposDeDatos(HechoInputDTO hecho){

        return hecho.getNombreUsuario() != null
                && hecho.getApellidoUsuario() != null
                && hecho.getFechaNacimientoUsuario() != null
                && hecho.getTitulo() != null
                && hecho.getDescripcion() != null
                && hecho.getCategoria() != null
                && hecho.getContenidoMultimedia() != null
                && hecho.getLatitud() != null
                && hecho.getLongitud() != null
                && hecho.getFechaAcontecimiento() != null;

    }

    private String tipoDeDatoErroneo(HechoInputDTO hecho){

        String datoErroneo = "";

        if(hecho.getNombreUsuario() == null)
            datoErroneo = "Nombre de Usuario";
        if(hecho.getApellidoUsuario() == null)
            datoErroneo = "Apellido de Usuario";
        if(hecho.getFechaNacimientoUsuario() == null)
            datoErroneo = "Fecha de Nacimiento";
        if(hecho.getTitulo() == null || hecho.getTitulo().isEmpty())
            datoErroneo = "Titulo";
        if(hecho.getDescripcion() == null || hecho.getDescripcion().isEmpty())
            datoErroneo = "Descripcion";
        if(hecho.getCategoria() == null || hecho.getCategoria().isEmpty())
            datoErroneo = "Categoria";
        if(hecho.getContenidoMultimedia() == null)
            datoErroneo = "Contenido Multimedia";
        if(hecho.getLatitud() == null)
            datoErroneo = "Latitud";
        if(hecho.getLongitud() == null)
            datoErroneo = "Longitud";
        if(hecho.getFechaAcontecimiento() == null)
            datoErroneo = "Fecha de Acontecimiento";

        return datoErroneo;

    }

    private Boolean verificarTiempoParaActualizar(HechoModificadoInputDTO hecho) {

        return this.userService.verificarTiempoParaActualizar(hecho);

    }
}