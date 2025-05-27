package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IHechoService;
import ar.edu.utn.frba.dds.fuenteEstatica.services.impl.HechoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechoController {
    private final IHechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public List<ArchivoOutputDTO> getAll() {
        return hechoService.getAll();
    }

    @GetMapping("/{id}")
    public HechoOutputDTO getById(@PathVariable("id") Long id){
        return hechoService.getById(id);
    }

    @PostMapping
    public HechoOutputDTO crearHecho(@RequestBody HechoEstatica hecho){
        return hechoService.crearHecho(hecho);
    }

    @DeleteMapping("/{id}")
    public void deleteHecho(@PathVariable("id") Long id){
        hechoService.deleteHecho(id);
    }
}
