package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Mostrar página de perfil del usuario
    @GetMapping
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    // Actualizar perfil del usuario
    @PostMapping
    public String actualizarPerfil(@ModelAttribute Usuario usuarioActualizado, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        
        try {
            // Actualizar datos del usuario (mantener ID)
            usuarioActualizado.setId(usuario.getId());
            usuarioActualizado.setTipoDoc(usuario.getTipoDoc()); // No permitir cambiar
            usuarioActualizado.setDni(usuario.getDni()); // No permitir cambiar
            usuarioActualizado.setFechaNacimiento(usuario.getFechaNacimiento()); // No permitir cambiar
            usuarioActualizado.setFechaRegistro(usuario.getFechaRegistro()); // No permitir cambiar
            
            if (usuarioService.actualizar(usuarioActualizado)) {
                // Recargar usuario desde BD
                Usuario usuarioRecargado = usuarioService.buscarPorId(usuario.getId());
                session.setAttribute("usuario", usuarioRecargado);
                model.addAttribute("success", "Perfil actualizado correctamente");
            }
            
            return "redirect:/perfil?success";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
            model.addAttribute("usuario", usuario);
            return "perfil";
        }
    }
}
