package com.example.Proyecto_Reverdecer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
public class UserController {

    private static final String DATOS_JSON = "src/main/resources/static/data/usuarios.json";
    private final ObjectMapper mapper = new ObjectMapper();

    // --- MOSTRAR REGISTRO ---
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // --- PROCESAR REGISTRO ---
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            File file = new File(DATOS_JSON);
            List<Usuario> usuarios = new ArrayList<>();

            if (file.exists() && file.length() > 0) {
                usuarios = mapper.readValue(file, new TypeReference<List<Usuario>>() {});
            }

            // Validar que no exista el correo
            if (usuarios.stream().anyMatch(u -> u.getCorreo() != null && u.getCorreo().equals(usuario.getCorreo()))) {
                model.addAttribute("error", "El correo ya está registrado");
                model.addAttribute("usuario", usuario);
                return "registro";
            }

            // Generar ID único
            long nuevoId = usuarios.stream()
                    .mapToLong(u -> u.getId() == null ? 0 : u.getId())
                    .max()
                    .orElse(0L) + 1L;
            usuario.setId(nuevoId);

            usuarios.add(usuario);

            // Crear directorio si no existe
            File dir = file.getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }

            // Guardar en JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, usuarios);

            model.addAttribute("mensaje", "Registro exitoso. Por favor inicia sesión.");
            return "redirect:/login";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al registrarse");
            model.addAttribute("usuario", usuario);
            return "registro";
        }
    }

    // --- MOSTRAR LOGIN ---
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    // --- PROCESAR LOGIN ---
    @PostMapping("/login")
    public String loginUsuario(@ModelAttribute Usuario usuario, Model model, HttpSession session) {
        try {
            File file = new File(DATOS_JSON);
            if (!file.exists() || file.length() == 0) {
                model.addAttribute("errorLogin", "No existen usuarios registrados");
                return "login";
            }

            List<Usuario> usuarios = mapper.readValue(file, new TypeReference<List<Usuario>>() {});

            Usuario encontrado = usuarios.stream()
                    .filter(u -> u.getCorreo() != null && u.getCorreo().equals(usuario.getCorreo())
                            && u.getPassword() != null && u.getPassword().equals(usuario.getPassword()))
                    .findFirst()
                    .orElse(null);

            if (encontrado != null) {
                session.setAttribute("usuario", encontrado);
                return "redirect:/";
            } else {
                model.addAttribute("errorLogin", "Correo o contraseña inválidos");
                return "login";
            }

        } catch (IOException e) {
            model.addAttribute("errorLogin", "Error al procesar login");
            return "login";
        }
    }

    // --- LOGOUT ---
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // --- MOSTRAR PERFIL ---
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    // --- GUARDAR PERFIL ---
    @PostMapping("/perfil")
    public String guardarPerfil(@ModelAttribute Usuario usuarioActualizado, HttpSession session, Model model) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

            if (usuarioSesion == null) {
                return "redirect:/login";
            }

            File file = new File(DATOS_JSON);
            if (!file.exists() || file.length() == 0) {
                model.addAttribute("error", "Error: archivo de usuarios no encontrado");
                return "perfil";
            }

            List<Usuario> usuarios = mapper.readValue(file, new TypeReference<List<Usuario>>() {});

            // Buscar y actualizar el usuario
            Usuario usuarioEncontrado = null;
            for (Usuario u : usuarios) {
                if (u.getId().equals(usuarioSesion.getId())) {
                    u.setNombres(usuarioActualizado.getNombres());
                    u.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
                    u.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
                    u.setCorreo(usuarioActualizado.getCorreo());
                    u.setNumero(usuarioActualizado.getNumero());
                    u.setDireccion1(usuarioActualizado.getDireccion1());
                    u.setDireccion2(usuarioActualizado.getDireccion2());
                    u.setGenero(usuarioActualizado.getGenero());
                    usuarioEncontrado = u;
                    break;
                }
            }

            // Guardar en JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, usuarios);

            // Actualizar sesión
            session.setAttribute("usuario", usuarioEncontrado);

            model.addAttribute("mensaje", "Perfil actualizado correctamente");
            model.addAttribute("usuario", usuarioEncontrado);
            return "perfil";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar cambios");
            return "perfil";
        }
    }
}
