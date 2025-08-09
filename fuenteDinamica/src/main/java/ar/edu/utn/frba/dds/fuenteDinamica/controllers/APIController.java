package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IAPIService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/fuenteDinamica")
@CrossOrigin(origins = "http://localhost:8081")
public class APIController {

    private IAPIService apiService;

    // Recoleccion de los hechos exclusiva para el agregador

    @GetMapping("/hechos")
    public List<HechoOutputDTO> buscarTodos(
            @RequestParam(required = false) Boolean enviado,
            @RequestParam(required = false) LocalDateTime dateTimeGT,
            @RequestParam(required = false) String titulo){
        List<HechoOutputDTO> hechos = apiService.buscarHechos(enviado,dateTimeGT);

        if (titulo == null || titulo.isEmpty()){
            return hechos;
        }else{
            return hechos.stream().filter(h -> h.getTitulo().equals(titulo)).toList();
            //TODO: El controller no deberia tener logica, pasarlo al service
        }
    }
}
