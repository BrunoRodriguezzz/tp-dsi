package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.auth.Rol;
import ar.edu.utn.frba.dds.client.dtos.auth.UsuarioDTO;
import ar.edu.utn.frba.dds.client.services.AuthApiService;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrarAdminController {
    private final AuthApiService authApiService;

    @Autowired
    public RegistrarAdminController(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/registroAdministrador")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrarAdministrador";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/registroAdministrador")
    public String registrarAdministrador(@ModelAttribute("usuario") UsuarioDTO usuario,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            System.out.println("El usuario es");
            System.out.println(usuario);
            usuario.setRoles(List.of(Rol.ADMINISTRADOR));
            usuario.setPermisos(new ArrayList<>());

            UsuarioDTO usuarioCreado = authApiService.crearUsuario(usuario);

            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente. Puedes iniciar sesión.");
            return "redirect:/home";

        } catch (Exception e) {
            model.addAttribute("usuario", new Usuario()); // Recargar objeto
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "registrarAdministrador";
        }
    }
}

