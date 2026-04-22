package com.example.Proyecto_Reverdecer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/sobre-nosotros")
    public String sobreNosotros() {
        return "sobre-nosotros";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/libro-reclamaciones")
    public String libroReclamaciones() {
        return "libro-reclamaciones";
    }
}
