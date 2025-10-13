package ar.edu.utn.frba.dds.client.controllers.advice;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        Object username = session.getAttribute("username");
        if (username != null) {
            model.addAttribute("usuarioLogueado", username);
        } else {
            model.addAttribute("usuarioLogueado", null);
        }
    }
}
