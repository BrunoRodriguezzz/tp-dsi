package ar.edu.utn.frba.dds.fuenteEstatica.clients;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputAgregadorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "agregador", configuration = ErrorConfigFeign.class)
public interface AgregadorClient {
    @PostMapping("/fuentes")
    ResponseEntity<ArchivoOutputAgregadorDTO> incorporarFuente(@RequestBody ArchivoOutputAgregadorDTO fuente);
}
