package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.services.HechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private HechoService hechoService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
        List<HechoDTO> hechosDestacados=
                this.hechoService.obtenerHechosDestacados().stream().limit(3).toList();
        // TODO: HARDCODEO etiquetas (faltan las etiquetas al agregador)
        System.out.println("Antes de agregar al modelo:");
        hechosDestacados.forEach(h -> {
            System.out.println("Hecho: " + h.getTitulo());
            System.out.println("Ubicacion es null? " + (h.getUbicacion() == null));
        });

        System.out.println("Cantidad de hechos: " + hechosDestacados.size());
        for (int i = 0; i < hechosDestacados.size(); i++) {
            HechoDTO hecho = hechosDestacados.get(i);
            System.out.println("Hecho " + i + ": " + hecho);
            if (hecho != null) {
                System.out.println("  - titulo: " + hecho.getTitulo());
                System.out.println("  - categoria: " + hecho.getCategoria());
                System.out.println("  - fecha: " + hecho.getFechaAcontecimiento());
                System.out.println("  - ubicacion: " + hecho.getUbicacion());
            }
        }

        List<String> hard = List.of("zaraza", "zaraza");
        hechosDestacados.forEach(h -> h.setEtiquetas(hard));
        model.addAttribute("hechosDestacados", hechosDestacados);
        return "landingPage/index";
    }
}