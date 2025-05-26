package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoNoAutorizado;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoProhibido;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorDeTiempo;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorTipoDeDatos;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoEliminarInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IDinamicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/fuenteDinamica")
@CrossOrigin(origins = "http://localhost:8080")
public class DinamicaController {

    @Autowired
    private IDinamicaService dinamicaService;

    // Uso del Agregador

    @GetMapping("/busqueda")
    public List<HechoOutputDTO> buscarTodos(
            @RequestParam(required = false) Boolean enviado,
            @RequestParam(required = false) LocalDateTime dateTimeGT){

        if(dateTimeGT != null){
            return dinamicaService.buscarHechos(enviado,dateTimeGT);
        }else{
            throw new ErrorTipoDeDatos("Error de ingreso de datos, se requiere indicar un filtro de horario.");
        }
    }

    // Uso de los Usuarios

    @PostMapping("/solicitud")
    public SolicitudOutputDTO solicitudCrearHecho(@RequestBody HechoInputDTO hecho){
        if(verificarEdadNecesaria(hecho)) {
            if(verificarTiposDeDatos(hecho)) {
                return dinamicaService.crear(hecho);
            }else{
                throw new ErrorTipoDeDatos("Error de ingreso de datos en: " + tipoDeDatoErroneo(hecho) + ". No puede haber campos vacios.");
            }
        }else{
            throw new ErrorAccesoProhibido("No cumple con la mayoria de edad.");
        }
    }

    @PatchMapping("/modificacion")
    public SolicitudOutputDTO actualizarHecho(@RequestBody HechoModificadoInputDTO hecho){
        if(verificarUsuarioRegistrado(hecho)){
            if(verificarTiempoParaActualizar(hecho)){
                return dinamicaService.actualizar(hecho);
            }else{
                throw new ErrorDeTiempo("El plazo para modificar el hecho se termino.");
            }
        }else{
            throw new ErrorAccesoNoAutorizado("Usuario no registrado.");
        }
    }

    // Uso de los Administradores

    @PatchMapping("/gestion")
    public HechoOutputDTO gestionarHecho(@RequestBody HechoRevisadoInputDTO hechoRevisado){

        return this.dinamicaService.gestionarHecho(hechoRevisado);

    }

    @PatchMapping("/eliminacion/{id}")
    public void eliminarHecho(@RequestBody HechoEliminarInputDTO hecho, @PathVariable Long id){
        this.dinamicaService.eliminar(hecho,id);
    }

    // Verificadores necesarios

    private Boolean verificarEdadNecesaria(HechoInputDTO hechoSolicitado){

        return this.dinamicaService.verificarEdadNecesaria(hechoSolicitado);
    }

    private Boolean verificarUsuarioRegistrado(HechoModificadoInputDTO hechoParaActualizar){

        return this.dinamicaService.verificarUsuarioRegistrado(hechoParaActualizar);
    }

    private Boolean verificarTiposDeDatos(HechoInputDTO hecho){

        return this.dinamicaService.verificarTiposDeDatos(hecho);

    }

    private String tipoDeDatoErroneo(HechoInputDTO hecho){

        return this.dinamicaService.tipoDeDatoErroneo(hecho);

    }

    private Boolean verificarTiempoParaActualizar(HechoModificadoInputDTO hecho) {

        return this.dinamicaService.verificarTiempoParaActualizar(hecho);

    }
}