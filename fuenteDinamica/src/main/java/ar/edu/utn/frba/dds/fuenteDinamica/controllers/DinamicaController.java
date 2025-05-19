package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IDinamicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class DinamicaController {

    @Autowired
    private IDinamicaService dinamicaService;

    // Uso del Agregador o cualquier Usuario

    @GetMapping
    public List<HechoOutputDTO> buscarTodos(@RequestParam(required = false) Boolean enviado){
        return dinamicaService.buscarHechos(enviado);
    }

    // Uso de los Usuarios

    @PostMapping
    public HechoOutputDTO solicitudCrearHecho(@RequestBody HechoInputDTO hecho){
        if(verificarEdadNecesaria(hecho)) {
            return dinamicaService.crear(hecho);
        }else{
            //TODO: Hay que agregar un mensaje o excepcion (no tiene la edad necesaria)
            return null;
        }
    }

    @PutMapping
    public HechoOutputDTO actualizarHecho(@RequestBody HechoModificadoInputDTO hecho){
        if(verificarUsuarioRegistrado(hecho)){
            return dinamicaService.actualizar(hecho);
        }else{
            //TODO: Hay que agregar un mensaje o excepcion (no hecho con ese ID a su nombre o no esta registrado)
            return null;
        }
    }

    // Uso de los Administradores

    @PutMapping
    public HechoOutputDTO gestionarHecho(@RequestBody Hecho hechoRevisado){
        return null;
    }

    // Verificadores necesarios

    private Boolean verificarEdadNecesaria(HechoInputDTO hechoSolicitado){

        return hechoSolicitado.getEdadUsuario() >= 18;
    }

    private Boolean verificarUsuarioRegistrado(HechoModificadoInputDTO hechoParaActualizar){

        Contribuyente usuario = new Contribuyente();
        usuario.setNombre(hechoParaActualizar.getNombreUsuario());
        usuario.setEdad(hechoParaActualizar.getEdadUsuario());

        return this.dinamicaService.verificarUsuarioRegistrado(usuario);
    }
}