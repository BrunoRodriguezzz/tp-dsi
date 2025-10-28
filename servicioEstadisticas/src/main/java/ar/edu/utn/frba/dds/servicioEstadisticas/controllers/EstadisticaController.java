package ar.edu.utn.frba.dds.servicioEstadisticas.controllers;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output.EstadisticaCategoriaDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output.EstadisticaProvinciaXColeccionDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output.EstadisticaSolicitudesDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.utils.EstadisticaCombinacion;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IEstadisticaService;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IImportadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        EstadisticaProvinciaXColeccionDTO estadisticaProvinciaXColeccion = this.estadisticaService.provinciaConMasHechosDeUnaColeccion(idColeccion);
        if(estadisticaProvinciaXColeccion == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(estadisticaProvinciaXColeccion);
    }

    @GetMapping("colecciones/provincias")
    public ResponseEntity provinciasConMasHechosPorColecciones(){
        List<EstadisticaProvinciaXColeccionDTO> estadisticas = this.estadisticaService.provinciasConMasHechosPorColecciones();
        if(estadisticas == null) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return ResponseEntity.status(HttpStatus.OK).body(estadisticas);
    }

    // ¿Cuál es la categoría con mayor cantidad de hechos reportados?
    @GetMapping("/categoria/mayorCantidadHechos")
    public ResponseEntity categoriasConMasHechos() {
        EstadisticaCategoriaDTO estadisticaCategoria = this.estadisticaService.categoriaConMasHechos();
        if(estadisticaCategoria == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.OK).body(estadisticaCategoria);
    }

    // ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/provincia/{idCategoria}/mayorCantidadHechos")
    public ResponseEntity provinciaConMasHechosSegunCategoria(
            @PathVariable("idCategoria") Long idCategoria
    ) {
        EstadisticaProvinciaXCategoria estadisticaProvinciaXCategoria = this.estadisticaService.provinciaConMasHechosSegunCategoria(idCategoria);
        if(estadisticaProvinciaXCategoria == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(estadisticaProvinciaXCategoria);
    }

    // ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    @GetMapping("/hora/{idCategoria}/mayorCantidadHechos")
    public ResponseEntity horaConMasHechosSegunCategoria(
            @PathVariable("idCategoria") Long idCategoria
    ) {
        EstadisticaHoraXCategoria estadisticaHoraXCategoria = this.estadisticaService.horaConMasHechosSegunCategoria(idCategoria);
        if(estadisticaHoraXCategoria == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(estadisticaHoraXCategoria);
    }

    // ¿Cuántas solicitudes de eliminación son spam?
    @GetMapping("/solicitudes/cantSpam")
    public ResponseEntity cantSolicitudesSpam() {
        EstadisticaSolicitudesDTO estadisticaSolicitudes = this.estadisticaService.cantSolicitudesSpam();
        if(estadisticaSolicitudes == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(estadisticaSolicitudes);
    }

    @PostMapping()
    public ResponseEntity calcularEstadisticas() {
        List<EstadisticaCombinacion> estadisticas = this.estadisticaService.calcularEstadisticas();
        return ResponseEntity.status(HttpStatus.CREATED).body(estadisticas);
    }

    @PostMapping("/csv")
    public ResponseEntity persistirCSV() {
        this.estadisticaService.persistirEnCSV();
        return ResponseEntity.status(HttpStatus.CREATED).body("Persistido");
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
