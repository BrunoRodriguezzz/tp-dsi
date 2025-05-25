package ar.edu.utn.frba.dds.fuenteProxy.controllers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hechos")
@CrossOrigin(origins = "http://localhost:8080")
public class HechoController {
    @Autowired
    private IHechoService hechoService;

    @GetMapping
    public List<OutputFuente> getAll() {
        return hechoService.getAll();
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<HechoDTO>> getWithFilters( //TODO: Verificar si es necesario el parseo
            @RequestParam(required = false) Long idHecho,
            @RequestParam(required = false) Long idFuente,
            @RequestParam(required = false) String fecha
    ) {
        // TODO: Validar parametros --> Validador tira Excepción
        List<HechoDTO> response = hechoService.getWithFilters(new FiltroProxy(idHecho, idFuente, fecha));
        if(response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);  // 200
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long idHecho) {
        hechoService.delete(idHecho);
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }
}
