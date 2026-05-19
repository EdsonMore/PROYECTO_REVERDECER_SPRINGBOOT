package com.example.Proyecto_Reverdecer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mapa")
public class MapController {

    @GetMapping
    public String mostrarMapa(Model model) {
        model.addAttribute("titulo", "Mapa Interactivo");
        return "mapa/mapa";
    }

    @GetMapping("/arboles")
    public String mostrarMapaArboles(Model model) {
        model.addAttribute("titulo", "Mapa de Árboles");
        return "mapa/mapa-arboles";
    }
}
