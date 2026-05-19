package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null) {
            return "redirect:/auth/login";
        }

        // Verificar si es admin
        if (usuario.getIsAdmin() == null || !usuario.getIsAdmin()) {
            return "redirect:/acceso-denegado";
        }

        // Permisos de módulos (admin tiene todos)
        model.addAttribute("puedeVerProductos", true);
        model.addAttribute("puedeVerUsuarios", true);
        model.addAttribute("puedeVerReportes", true);
        
        // Permisos de acciones (admin tiene todos)
        model.addAttribute("puedeCrear", true);
        model.addAttribute("puedeEditar", true);
        model.addAttribute("puedeEliminar", true);
        model.addAttribute("puedeVerVentas", true);
        
        model.addAttribute("usuario", usuario);
        
        return "admin/dashboard";
    }
}
