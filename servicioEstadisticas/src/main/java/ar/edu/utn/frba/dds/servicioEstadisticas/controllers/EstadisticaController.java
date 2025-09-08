package ar.edu.utn.frba.dds.servicioEstadisticas.controllers;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.*;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IEstadisticaService;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IImportadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/estadisticas")
@CrossOrigin(origins = "http://localhost:8085")
public class EstadisticaController {
    @Autowired
    private IEstadisticaService estadisticaService;

    @Autowired
    private IImportadorService importadorService;

    // De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
    @GetMapping("/coleccion/{idColeccion}/provincia/mayorCantidadHechos")
    public ResponseEntity provinciaConMasHechosDeUnaColeccion(
            @PathVariable("idColeccion") Long idColeccion
    ) {
        Provincia provincia = this.estadisticaService.provinciaConMasHechosDeUnaColeccion(idColeccion);
        if(provincia == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(provincia);
    }

    // ¿Cuál es la categoría con mayor cantidad de hechos reportados?
    @GetMapping("/categoria/mayorCantidadHechos")
    public ResponseEntity categoriaConMasHechos() {
        Categoria categoria = this.estadisticaService.categoriaConMasHechos();
        if(categoria == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.OK).body(categoria);
    }

    // ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/provincia/{idCategoria}/mayorCantidadHechos")
    public ResponseEntity provinciaConMasHechosSegunCategoria(
            @PathVariable("idCategoria") Long idCategoria
    ) {
        Provincia provincia = this.estadisticaService.provinciaConMasHechosSegunCategoria(idCategoria);
        if(provincia == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(provincia);
    }

    // ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/hora/{idCategoria}/mayorCantidadHechos")
    public ResponseEntity horaConMasHechosSegunCategoria(
            @PathVariable("idCategoria") Long idCategoria
    ) {
        LocalTime hora = this.estadisticaService.horaConMasHechosSegunCategoria(idCategoria);
        if(hora == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(hora);
    }

    // ¿Cuántas solicitudes de eliminación son spam?
    @GetMapping("/solicitudes/cantSpam")
    public ResponseEntity cantSolicitudesSpam() {
        Long cant = this.estadisticaService.cantSolicitudesSpam();
        if(cant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cant);
    }

    @PostMapping()
    public ResponseEntity calcularEstadisticas() {
        List<EstadisticaCombinacion> estadisticas = this.estadisticaService.calcularEstadisticas();
        return ResponseEntity.status(HttpStatus.CREATED).body(estadisticas);
    }

    // ---------------------------------- TEST ------------------------------------
    @GetMapping("/hechos")
    public ResponseEntity getHechos() {
        List<HechoInputDTO> hechos = this.importadorService.importarHechos();
        if(hechos == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(hechos);
    }

    @GetMapping("/colecciones")
    public ResponseEntity getColecciones() {
        List<ColeccionInputDTO> colecciones = this.importadorService.importarColecciones();
        if(colecciones == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(colecciones);
    }
}
