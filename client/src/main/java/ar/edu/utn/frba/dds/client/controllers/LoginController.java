package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.auth.UsuarioDTO;
import ar.edu.utn.frba.dds.client.services.AuthApiService;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Permiso;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Rol;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {
    private final AuthApiService authApiService;

    @Autowired
    public LoginController(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute("usuario") UsuarioDTO usuario,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            // TODO: ESTO ESTÁ HARDCODEADO, DEFINIR DÓNDE PONER LOS ROLES Y CUÁLES
            usuario.setRoles(List.of(Rol.ROL_1));
            usuario.setPermisos(List.of(Permiso.PERMISO_2));

            UsuarioDTO usuarioCreado = authApiService.crearUsuario(usuario);

            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente. Puedes iniciar sesión.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("usuario", new Usuario()); // Recargar objeto
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // Método para proporcionar el usuario logueado a todas las vistas
    @ModelAttribute("usuarioLogueado")
    public String usuarioLogueado(HttpSession session) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if(request.getSession().getAttribute("username") == null) {
            return null;
        }

        return request.getSession().getAttribute("username").toString();
    }
}
