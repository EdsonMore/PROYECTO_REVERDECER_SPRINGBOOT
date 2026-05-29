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

    //servicio para manejar usuarios
    private final UsuarioService usuarioService;

    //inyeccion del servicio de usuarios
    public UserController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //muestra el formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    //registra un nuevo usuario
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        boolean exito = usuarioService.registrar(usuario);
        if (!exito) {
            model.addAttribute("error", "El correo ya está registrado");
            return "registro";
        }
        return "redirect:/auth/login?success";
    }


    //muestra el formulario de iniciar sesión
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    //autentica al usuario y crea la sesión
    @PostMapping("/login")
    public String loginUsuario(@ModelAttribute Usuario usuario, Model model, HttpSession session) {
        Usuario encontrado = usuarioService.autenticar(usuario.getCorreo(), usuario.getPassword());

        if (encontrado != null) {
            session.setAttribute("usuario", encontrado);

            if (Boolean.TRUE.equals(encontrado.getIsAdmin())) {
                return "redirect:/admin/dashboard";
            } else if ("ROLE_GESTOR_AMBIENTAL".equals(encontrado.getRol())) {
                return "redirect:/gestor/estadisticas";
            }
            return "redirect:/";
        }

        model.addAttribute("errorLogin", "Correo o contraseña incorrectos");
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    //cierra la sesión del usuario
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login?logout";
    }
}