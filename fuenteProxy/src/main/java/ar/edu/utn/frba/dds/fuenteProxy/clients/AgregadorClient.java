package ar.edu.utn.frba.dds.fuenteProxy.clients;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output.OutputFuenteAgregador;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "agregador", configuration = ErrorConfigFeign.class)
public interface AgregadorClient {
    @PostMapping("/fuentes")
    ResponseEntity<OutputFuenteAgregador> incorporarFuente(@RequestBody OutputFuenteAgregador fuente);
}
