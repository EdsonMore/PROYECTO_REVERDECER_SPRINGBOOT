// UserController.java - Solo maneja autenticación
package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class UserController {

    private final UsuarioService usuarioService;

    public UserController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        boolean exito = usuarioService.registrar(usuario);
        if (!exito) {
            model.addAttribute("error", "El correo ya está registrado");
            return "registro";
        }
        return "redirect:/auth/login?success";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String loginUsuario(@ModelAttribute Usuario usuario, Model model, HttpSession session) {
        Usuario encontrado = usuarioService.autenticar(usuario.getCorreo(), usuario.getPassword());

        if (encontrado != null) {
            session.setAttribute("usuario", encontrado);

            if (Boolean.TRUE.equals(encontrado.getIsAdmin())) {
                return "redirect:/admin/dashboard";
            } else if ("ROLE_GESTOR_AMBIENTAL".equals(encontrado.getRol())) {
                return "redirect:/gestor/dashboard";
            }
            return "redirect:/";
        }

        model.addAttribute("errorLogin", "Correo o contraseña incorrectos");
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login?logout";
    }
}