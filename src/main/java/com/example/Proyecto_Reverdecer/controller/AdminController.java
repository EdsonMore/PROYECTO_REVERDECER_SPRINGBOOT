package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import com.example.Proyecto_Reverdecer.service.GestorAmbientalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final GestorAmbientalService gestorAmbientalService;

    public AdminController(UsuarioRepository usuarioRepository, GestorAmbientalService gestorAmbientalService) {
        this.usuarioRepository = usuarioRepository;
        this.gestorAmbientalService = gestorAmbientalService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        model.addAttribute("usuario", usuario);
        model.addAttribute("resumen", gestorAmbientalService.resumenDashboard());
        model.addAttribute("arbolesPorEstado", gestorAmbientalService.arbolesPorEstado());
        model.addAttribute("alertasPorZona", gestorAmbientalService.alertasPorZona());
        model.addAttribute("totalUsuarios", usuarioRepository.count());

        model.addAttribute("puedeVerArboles", true);
        model.addAttribute("puedeVerUsuarios", false);
        model.addAttribute("puedeVerReportes", true);
        model.addAttribute("puedeVerIoT", false);
        model.addAttribute("puedeVerRiego", false);
        model.addAttribute("puedeVerAlertas", false);

        model.addAttribute("puedeCrear", true);
        model.addAttribute("puedeEditar", true);
        model.addAttribute("puedeEliminar", true);
        model.addAttribute("puedeConfigurar", false);

        return "admin/dashboard";
    }
}
