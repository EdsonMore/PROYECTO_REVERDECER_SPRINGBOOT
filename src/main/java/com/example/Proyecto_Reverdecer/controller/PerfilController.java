// PerfilController.java - Solo maneja perfil
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

    @GetMapping
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping
    public String actualizarPerfil(@ModelAttribute Usuario usuarioActualizado,
            HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }

        try {
            usuarioActualizado.setId(usuario.getId());
            usuarioActualizado.setTipoDoc(usuario.getTipoDoc());
            usuarioActualizado.setDni(usuario.getDni());
            usuarioActualizado.setFechaNacimiento(usuario.getFechaNacimiento());
            usuarioActualizado.setFechaRegistro(usuario.getFechaRegistro());
            usuarioActualizado.setIsAdmin(usuario.getIsAdmin()); 
            usuarioActualizado.setRol(usuario.getRol()); 

            if (usuarioService.actualizar(usuarioActualizado)) {
                Usuario usuarioRecargado = usuarioService.buscarPorId(usuario.getId());
                session.setAttribute("usuario", usuarioRecargado);
                return "redirect:/perfil?success";
            }

            model.addAttribute("error", "Error al actualizar el perfil");
            model.addAttribute("usuario", usuario);
            return "perfil";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("usuario", usuario);
            return "perfil";
        }
    }
}