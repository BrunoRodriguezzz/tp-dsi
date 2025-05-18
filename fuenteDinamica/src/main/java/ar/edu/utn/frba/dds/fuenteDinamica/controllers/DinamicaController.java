package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IDinamicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class DinamicaController {

    @Autowired
    private IDinamicaService dinamicaService;

    @GetMapping
    public List<HechoOutputDTO> buscarTodos(@RequestParam(required = false) Boolean enviado){
        return dinamicaService.buscarHechos(enviado);
    }
}