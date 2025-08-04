package ar.edu.utn.frba.dds.fuenteProxy.controllers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechoController {
    @Autowired
    private IHechoService hechoService;

    @GetMapping
    public ResponseEntity<List<OutputFuente>> getAll() {
        List<OutputFuente> response = hechoService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<OutputFuente>> getWithFilters(@ModelAttribute FiltroProxy filtro) {
        List<OutputFuente> response = hechoService.getWithFilters(filtro);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        hechoService.delete(id);
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }
}
