package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoEliminarInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoRevisadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteDinamica")
@CrossOrigin(origins = "http://localhost:8081")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    // Gestion del hecho (aceptacion, sugerir cambios o rechazar)

    @PostMapping("/gestion")
    public HechoOutputDTO gestionarHecho(@RequestBody HechoRevisadoInputDTO hechoRevisado){
        return this.adminService.gestionarHecho(hechoRevisado);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<HechoOutputDTO>> obtenerPendientes() {
        List<HechoOutputDTO> hechosPendientes = this.adminService.obtenerHechosPendientes();
        return ResponseEntity.ok(hechosPendientes);
    }

    @GetMapping("/pendientes/{id}")
    public ResponseEntity<HechoOutputDTO> obtenerPendiente(@PathVariable(name = "id") Long id) {
        HechoOutputDTO hechoPendiente = this.adminService.obtenerHechoPendiente(id);
        return ResponseEntity.ok(hechoPendiente);
    }


    // Eliminacion del hecho de manera directa

    @PatchMapping("/eliminacion/{id}")
    public void eliminarHecho(@PathVariable Long id){
        this.adminService.eliminar(id);
    }
}