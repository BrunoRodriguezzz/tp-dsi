package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/fuenteDinamica")
@CrossOrigin(origins = "http://localhost:8081")
public class APIController {

    @Autowired
    private IAPIService apiService;

    // Recoleccion de los hechos exclusiva para el agregador

    @GetMapping("/hechos")
    public List<HechoOutputDTO> buscarTodos(
            @RequestParam(required = false) Boolean enviado,
            @RequestParam(required = false) LocalDateTime dateTimeGT,
            @RequestParam(required = false) String titulo){

        if (titulo == null || titulo.isEmpty()){
            return this.apiService.buscarHechos(enviado,dateTimeGT);
        }else{
            return this.apiService.hechosDeIgualTitulo(enviado,dateTimeGT,titulo);
        }
    }
}