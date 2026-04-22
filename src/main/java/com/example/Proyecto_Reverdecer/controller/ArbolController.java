package com.example.Proyecto_Reverdecer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.model.Arbol;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
@RequestMapping("/arboles")
public class ArbolController {

    private static final String DATOS_JSON = "src/main/resources/static/data/usuarios.json";
    private final ObjectMapper mapper = new ObjectMapper();

    // --- REDIRIGIR A LISTADO ---
    @GetMapping("")
    public String redirigirAListado() {
        return "redirect:/arboles/listado";
    }

    // --- LISTAR ÁRBOLES ---
    @GetMapping("/listado")
    public String listarArboles(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("arboles", usuario.getArboles());
        return "arboles/listado";
    }

    // --- MOSTRAR FORMULARIO DE REGISTRO ---
    @GetMapping("/registro")
    public String mostrarRegistroArbol(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("arbol", new Arbol());
        return "arboles/registro";
    }

    // --- PROCESAR REGISTRO DE ÁRBOL ---
    @PostMapping("/registro")
    public String registrarArbol(@ModelAttribute Arbol arbol, HttpSession session, Model model) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

            if (usuarioSesion == null) {
                return "redirect:/login";
            }

            File file = new File(DATOS_JSON);
            if (!file.exists() || file.length() == 0) {
                model.addAttribute("error", "Error: archivo de usuarios no encontrado");
                return "arboles/registro";
            }

            List<Usuario> usuarios = mapper.readValue(file, new TypeReference<List<Usuario>>() {});

            // Buscar el usuario actual y agregar el árbol
            Usuario usuarioEncontrado = null;
            for (Usuario u : usuarios) {
                if (u.getId().equals(usuarioSesion.getId())) {
                    // Generar ID único para el árbol
                    long nuevoId = u.getArboles().stream()
                            .mapToLong(a -> a.getId() == null ? 0 : a.getId())
                            .max()
                            .orElse(0L) + 1L;
                    arbol.setId(nuevoId);

                    u.getArboles().add(arbol);
                    usuarioEncontrado = u;
                    break;
                }
            }

            if (usuarioEncontrado == null) {
                model.addAttribute("error", "Usuario no encontrado");
                return "arboles/registro";
            }

            // Guardar en JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, usuarios);

            // Actualizar sesión
            session.setAttribute("usuario", usuarioEncontrado);

            model.addAttribute("mensaje", "Árbol registrado correctamente");
            return "redirect:/arboles/listado";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al registrar árbol");
            return "arboles/registro";
        }
    }

    // --- ELIMINAR ÁRBOL ---
    @GetMapping("/eliminar/{id}")
    public String eliminarArbol(@PathVariable Long id, HttpSession session, Model model) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

            if (usuarioSesion == null) {
                return "redirect:/login";
            }

            File file = new File(DATOS_JSON);
            if (!file.exists() || file.length() == 0) {
                model.addAttribute("error", "Error: archivo de usuarios no encontrado");
                return "redirect:/arboles/listado";
            }

            List<Usuario> usuarios = mapper.readValue(file, new TypeReference<List<Usuario>>() {});

            // Buscar el usuario y eliminar el árbol
            for (Usuario u : usuarios) {
                if (u.getId().equals(usuarioSesion.getId())) {
                    u.getArboles().removeIf(a -> a.getId().equals(id));
                    // Guardar en JSON
                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, usuarios);
                    // Actualizar sesión
                    session.setAttribute("usuario", u);
                    break;
                }
            }

            return "redirect:/arboles/listado";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al eliminar árbol");
            return "redirect:/arboles/listado";
        }
    }
}
