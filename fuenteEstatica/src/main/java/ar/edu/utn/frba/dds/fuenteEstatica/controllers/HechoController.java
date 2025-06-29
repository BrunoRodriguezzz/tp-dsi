package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IHechoService;
import ar.edu.utn.frba.dds.fuenteEstatica.services.impl.HechoService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechoController {
    private final IHechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping()
    public ResponseEntity<List<ArchivoOutputDTO>> getByTitle(
        @RequestParam(name = "titulo", required = false) String title,
        @RequestParam(name = "id", required = false) Long idFuente
    ) {
        if(title != null && !title.isEmpty() && idFuente != null && idFuente > 0) {
            List<ArchivoOutputDTO> outputDTO = this.hechoService.getByTitleAndIdFuente(title, idFuente);
            return new ResponseEntity<>(outputDTO, HttpStatus.OK);
        }
        else {
            List<ArchivoOutputDTO> response = hechoService.getAll();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<ArchivoOutputDTO>> getWithFilters(@ModelAttribute FiltroEstatica filtro) {
        List<ArchivoOutputDTO> response = hechoService.getWithFilters(filtro);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchivoOutputDTO> getById(@PathVariable("id") Long id){
        ArchivoOutputDTO response = hechoService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHecho(@PathVariable("id") Long id){
        hechoService.deleteHecho(id);
        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }
}
