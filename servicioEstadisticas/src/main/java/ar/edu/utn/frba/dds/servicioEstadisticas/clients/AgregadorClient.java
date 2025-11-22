package ar.edu.utn.frba.dds.servicioEstadisticas.clients;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionPageDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoPageDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;


@FeignClient(name = "agregador", configuration = ErrorConfigFeign.class)
public interface AgregadorClient {
    @GetMapping("/colecciones")
    ResponseEntity<ColeccionPageDTO> buscarColecciones(@RequestParam(name = "all", required = false) Boolean all);

    @GetMapping("/colecciones/{id}/hechos")
    ResponseEntity<HechoPageDTO> buscarHechosColeccion(
            @PathVariable("id") Long id,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "fechaAcontecimientoInicio", required = false) LocalDateTime fechaAcontecimientoInicio,
            @RequestParam(name = "fechaAcontecimientoFin", required = false) LocalDateTime fechaAcontecimientoFin,
            @RequestParam(name = "titulo", required = false) String titulo,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud,
            @RequestParam(name = "fechaCargaInicio", required = false) LocalDateTime fechaCargaInicio,
            @RequestParam(name = "fechaCargaFin", required = false) LocalDateTime fechaCargaFin,
            @RequestParam(name = "fuente", required = false) String fuente,
            @RequestParam(name = "all", required = false) Boolean all,
            Pageable pageable
    );

    @GetMapping("/hechos/independientes")
    ResponseEntity<List<HechoInputDTO>> buscarHechos();

    @GetMapping()
    ResponseEntity<List<SolicitudEliminacionInputDTO>> buscarSolicitudes();
}
