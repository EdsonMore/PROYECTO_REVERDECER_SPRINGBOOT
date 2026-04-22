package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/auth") // Agregamos un RequestMapping base para orden
public class UserController {

    private final UsuarioService usuarioService;

    // INYECCIÓN POR CONSTRUCTOR: Esto es lo que pide la rúbrica específicamente
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
        try {
            boolean exito = usuarioService.registrar(usuario);
            if (!exito) {
                model.addAttribute("error", "El correo ya está registrado");
                return "registro";
            }
            return "redirect:/auth/login?success";
        } catch (IOException e) {
            model.addAttribute("error", "Error en el servidor");
            return "registro";
        }
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
            return "redirect:/";
        }
        model.addAttribute("errorLogin", "Credenciales incorrectas");
        return "login";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/auth/login";
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute Usuario usuarioActualizado, HttpSession session, Model model) {
        try {
            Usuario sessionUser = (Usuario) session.getAttribute("usuario");
            if (sessionUser == null) return "redirect:/auth/login";
            
            usuarioActualizado.setId(sessionUser.getId());
            Usuario resultado = usuarioService.actualizar(usuarioActualizado);
            
            session.setAttribute("usuario", resultado);
            model.addAttribute("mensaje", "Perfil actualizado");
            model.addAttribute("usuario", resultado);
            return "perfil";
        } catch (IOException e) {
            model.addAttribute("error", "Error al actualizar");
            return "perfil";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}

/**
 * CONTROLADOR ADICIONAL: Manejo de rutas sin /auth
 * Para compatibilidad con URLs directas: /login, /registro
 */
@Controller
class AuthController {
    
    private final UsuarioService usuarioService;
    
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    // Redireccionar /login a /auth/login
    @GetMapping("/login")
    public String loginDirecto(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }
    
    @PostMapping("/login")
    public String procesarLoginDirecto(@ModelAttribute Usuario usuario, Model model, HttpSession session) {
        Usuario encontrado = usuarioService.autenticar(usuario.getCorreo(), usuario.getPassword());
        if (encontrado != null) {
            session.setAttribute("usuario", encontrado);
            return "redirect:/";
        }
        model.addAttribute("errorLogin", "Credenciales incorrectas");
        return "login";
    }
    
    // Redireccionar /registro a /auth/registro
    @GetMapping("/registro")
    public String registroDirecto(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }
    
    @PostMapping("/registro")
    public String procesarRegistroDirecto(@ModelAttribute Usuario usuario, Model model) {
        try {
            boolean exito = usuarioService.registrar(usuario);
            if (!exito) {
                model.addAttribute("error", "El correo ya está registrado");
                return "registro";
            }
            return "redirect:/login?success";
        } catch (IOException e) {
            model.addAttribute("error", "Error en el servidor");
            return "registro";
        }
    }
    
    // Logout - Cerrar sesión
    @GetMapping("/logout")
    public String logoutDirecto(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}