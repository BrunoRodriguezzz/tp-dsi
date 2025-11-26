package ar.edu.utn.frba.dds.fuenteDinamica.clients;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// , configuration = ErrorConfigFeign.class
@FeignClient(name = "agregador")
public interface AgregadorClient {
    @PostMapping("/fuentes")
    ResponseEntity<FuenteOutputDTO> incorporarFuente(@RequestBody FuenteOutputDTO fuente);

    @PutMapping("/hechos/contribuyentes/{id}")
    ResponseEntity<Void> actualizarHechoDinamica(@RequestBody HechoOutputDTO hecho, @PathVariable(name = "id") Long id);

    @PostMapping("/hechos")
    ResponseEntity<List<String>> incorporarHecho(@RequestBody HechoOutputDTO hecho);
}
