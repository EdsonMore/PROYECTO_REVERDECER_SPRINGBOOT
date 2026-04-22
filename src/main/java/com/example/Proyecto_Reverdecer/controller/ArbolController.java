package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Arbol;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.ArbolService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/arboles")
public class ArbolController {

    private final ArbolService arbolService;

    // Inyección por constructor
    public ArbolController(ArbolService arbolService) {
        this.arbolService = arbolService;
    }

    // Redirigir /arboles a /arboles/listado
    @GetMapping("")
    public String redirigir() {
        return "redirect:/arboles/listado";
    }

    // Listar todos los árboles
    @GetMapping("/listado")
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("arboles", arbolService.listarTodos());
        return "arboles/listado";
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormulario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("arbol", new Arbol());
        return "arboles/registro";
    }

    // Guardar nuevo árbol (POST)
    @PostMapping("/registro")
    public String guardar(
            HttpSession session,
            @RequestParam String especie,
            @RequestParam String ubicacion,
            @RequestParam String fecha,
            @RequestParam String estado,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }

        // Validación
        if (especie == null || especie.trim().isEmpty()) {
            model.addAttribute("error", "La especie es obligatoria");
            model.addAttribute("arboles", arbolService.listarTodos());
            return "arboles/listado";
        }

        // Crear y guardar el árbol
        Arbol arbol = new Arbol();
        arbol.setEspecie(especie);
        arbol.setUbicacion(ubicacion);
        arbol.setFecha(LocalDate.parse(fecha));
        arbol.setEstado(estado);

        arbolService.guardar(arbol);
        return "redirect:/arboles/listado";
    }

    // Eliminar árbol
    @GetMapping("/eliminar/{id}")
    public String eliminar(HttpSession session, @PathVariable Long id) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        arbolService.eliminar(id);
        return "redirect:/arboles/listado";
    }
}