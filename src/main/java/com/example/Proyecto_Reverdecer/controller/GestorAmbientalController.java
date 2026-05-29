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


    //servicio para las estadisticas
    @Autowired
    private GestorAmbientalService gestorService;

    //verifica que el usuario es gestor ambiental
    private Usuario verificarAcceso(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return null;
        if (!usuario.esGestorAmbiental()) return null;
        return usuario;
    }

    //muestra estadisticas generales de los arboles

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

    //muestra zonas criticas 

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
}
