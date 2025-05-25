package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.HechoDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.services.impl.HechoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechoController {
    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public List<HechoDTO> findAll() {
        return hechoService.findAll();
    }
}
