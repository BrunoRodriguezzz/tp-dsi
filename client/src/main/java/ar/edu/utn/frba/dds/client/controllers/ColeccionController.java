package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.services.ColeccionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ColeccionController {
  private final Logger LOGGER = LogManager.getLogger(ColeccionController.class);
  private final ColeccionService coleccionService;

//  @GetMapping
//  public String listarHechos(Model model) {
//    model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
//    List<HechoDTO> hechos = this.hechoService.obtenerHechos();
//    model.addAttribute("hechos", hechos);
//    model.addAttribute("cantidad", hechos.size());
//    LOGGER.info("Mostrando {} hechos.", hechos.size());
//    return "hechos";
//  }
//
//  @GetMapping("/{id}")
//  public String verDetalleHecho(@PathVariable Long id, Model model) {
//    HechoDTO hecho = hechoService.obtenerHechoPorId(id);
//    LOGGER.info("Mostrando {} hecho.", id);
//    model.addAttribute("hecho", hecho);
//    model.addAttribute("titulo", hecho.getTitulo());
//    return "hecho";
//  }

  @GetMapping("/colecciones")
  public String listarColecciones(Model model) {
    List<ColeccionDTO> colecciones = coleccionService.obtenerColecciones();
    model.addAttribute("colecciones", colecciones);
    return "colecciones";
  }

  @GetMapping("/coleccion/{id}")
  public String verDetalleColeccion(@PathVariable Long id, Model model) {
    ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(id);
    model.addAttribute("coleccion", coleccion);
    return "coleccion";
  }

  @GetMapping("/coleccion/{id}/hechos")
  @ResponseBody
  public List<HechoDTO> hechosDeColeccion(@PathVariable Long id) {
    return coleccionService.obtenerHechosPorColeccionId(id);
  }

  @GetMapping("/editarColeccion/{id}")
  public String editarColeccion(@PathVariable Long id, Model model) {
    ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(id);
    model.addAttribute("coleccion", coleccion);
    return "editarColeccion";
  }
}
