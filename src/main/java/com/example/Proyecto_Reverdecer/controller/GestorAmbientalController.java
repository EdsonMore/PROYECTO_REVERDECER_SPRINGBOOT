package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.GestorAmbientalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gestor")
public class GestorAmbientalController {

    @Autowired
    private GestorAmbientalService gestorService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/auth/login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("resumen", gestorService.resumenDashboard());
        model.addAttribute("arbolesPorEstado", gestorService.arbolesPorEstado());
        model.addAttribute("alertasPorZona", gestorService.alertasPorZona());

        return "gestor/dashboardGestor";
    }

    @GetMapping("/estadisticas")
    public String estadisticas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/auth/login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("arbolesPorEspecie", gestorService.arbolesPorEspecie());
        model.addAttribute("arbolesPorZona", gestorService.arbolesPorZona());
        model.addAttribute("arbolesPorEstado", gestorService.arbolesPorEstado());
        model.addAttribute("porcentajeRiesgo", gestorService.porcentajeEnRiesgo());
        model.addAttribute("totalArboles", gestorService.totalArboles());

        return "gestor/estadisticas";
    }

    @GetMapping("/zonas-criticas")
    public String zonasCriticas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/auth/login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("zonasCriticas", gestorService.zonasCriticas());
        model.addAttribute("porcentajeRiesgoPorZona", gestorService.porcentajeRiesgoPorZona());
        model.addAttribute("alertasPorZona", gestorService.alertasPorZona());
        model.addAttribute("arbolesConAlertas", gestorService.arbolesConAlertas());

        return "gestor/zonas-criticas";
    }

    @GetMapping("/analisis-especies")
    public String analisisEspecies(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/auth/login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("especiesSupervivencia", gestorService.especiesConMejorSupervivencia());
        model.addAttribute("especiesActivas", gestorService.especiesActivasPorConteo());
        model.addAttribute("arbolesPorEspecie", gestorService.arbolesPorEspecie());

        return "gestor/analisis-especies";
    }

    @GetMapping("/reportes")
    public String reportes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null)
            return "redirect:/auth/login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("resumen", gestorService.resumenDashboard());
        model.addAttribute("arbolesPorEspecie", gestorService.arbolesPorEspecie());
        model.addAttribute("arbolesPorZona", gestorService.arbolesPorZona());
        model.addAttribute("zonasCriticas", gestorService.zonasCriticas());
        model.addAttribute("especiesSupervivencia", gestorService.especiesConMejorSupervivencia());
        model.addAttribute("arbolesConAlertas", gestorService.arbolesConAlertas());

        return "gestor/reportes";
    }
}
