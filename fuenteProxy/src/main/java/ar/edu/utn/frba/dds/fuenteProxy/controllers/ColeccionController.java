package ar.edu.utn.frba.dds.fuenteProxy.controllers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.impl.ColeccionService;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputHecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/colecciones")
public class ColeccionController {

    @Autowired
    private ColeccionService coleccionService;

    @GetMapping
    public ResponseEntity<List<OutputColeccionDTO>> getAll() {
        List<OutputColeccionDTO> outputColeccionDTO = coleccionService.getAll();
        return new ResponseEntity<>(outputColeccionDTO, HttpStatus.OK);
    }

    @GetMapping("/{identificador}/hechos")
    public ResponseEntity<List<OutputHecho>> getHechosByColeccion(@PathVariable Long identificador) {
        List<OutputHecho> hechos = coleccionService.getHechosByColeccion(identificador);
        if (hechos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(hechos);
    }
}
