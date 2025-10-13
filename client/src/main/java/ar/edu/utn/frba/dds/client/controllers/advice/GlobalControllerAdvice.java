package ar.edu.utn.frba.dds.client.controllers.advice;

import ar.edu.utn.frba.dds.client.dtos.auth.UsuarioDTO;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {

        Object usernameObj = session.getAttribute("username");
        Object idObj = session.getAttribute("id");
        Object nombreObj = session.getAttribute("nombre");
        Object apellidoObj = session.getAttribute("apellido");
        Object fechaNacimientoObj = session.getAttribute("fechaNacimiento");

        if (usernameObj != null && idObj != null && nombreObj != null && apellidoObj != null && fechaNacimientoObj != null) {

            UsuarioDTO usuario = UsuarioDTO.builder()
                .username(usernameObj.toString())
                .id(Long.parseLong(idObj.toString()))
                .nombre(nombreObj.toString())
                .apellido(apellidoObj.toString())
                .fechaNacimiento(LocalDate.parse(fechaNacimientoObj.toString()))
                .build();

            model.addAttribute("usuarioLogueado", usuario);
        } else {
            model.addAttribute("usuarioLogueado", null);
        }
    }
}
