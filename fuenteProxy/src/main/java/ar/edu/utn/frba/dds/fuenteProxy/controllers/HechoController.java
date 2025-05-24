package ar.edu.utn.frba.dds.fuenteProxy.controllers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/hechos")
@CrossOrigin(origins = "http://localhost:8081")
public class HechoController {
    @Autowired
    private IHechoService hechoService;

    public List<HechoDTO> getAll() {
        //TODO
        return List.of();
    }

    public List<HechoDTO> getNew(Date fecha) {
        //TODO
        return List.of();
    }

    public HechoDTO getHecho(Long id) {
        //TODO
        return hechoService.getById(id);
    }

    public List<HechoDTO> getAllFuente(Long id) {
        //TODO
        return List.of();
    }
}
