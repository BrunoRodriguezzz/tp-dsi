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
        if(verificarEdadNecesaria(hecho)) {
            if(verificarTiposDeDatos(hecho)) {
                return userService.crear(hecho);
            }else{
                throw new ErrorTipoDeDatos("Error de ingreso de datos en: " + tipoDeDatoErroneo(hecho) + ". No puede haber campos vacios.");
            }
        }else{
            throw new ErrorAccesoProhibido("No cumple con la mayoria de edad.");
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
    }

    // Verificadores necesarios

    private Boolean verificarEdadNecesaria(HechoInputDTO hechoSolicitado){

        return this.userService.verificarEdadNecesaria(hechoSolicitado);
    }

    private Boolean verificarUsuarioRegistrado(HechoModificadoInputDTO hechoParaActualizar){

        return this.userService.verificarUsuarioRegistrado(hechoParaActualizar);
    }

    private Boolean verificarTiposDeDatos(HechoInputDTO hecho){

        return this.userService.verificarTiposDeDatos(hecho);

    }

    private String tipoDeDatoErroneo(HechoInputDTO hecho){

        return this.userService.tipoDeDatoErroneo(hecho);

    }

    private Boolean verificarTiempoParaActualizar(HechoModificadoInputDTO hecho) {

        return this.userService.verificarTiempoParaActualizar(hecho);

    }
}