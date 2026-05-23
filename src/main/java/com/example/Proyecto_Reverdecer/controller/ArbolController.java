package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Arbol;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.ArbolService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/arboles")
public class ArbolController {

    private final ArbolService arbolService;
    private final UsuarioService usuarioService;

    public ArbolController(ArbolService arbolService, UsuarioService usuarioService) {
        this.arbolService = arbolService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("")
    public String redirigir() {
        return "redirect:/arboles/listado";
    }

    // Listar solo los árboles del usuario
    @GetMapping("/listado")
    public String listar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("arboles", arbolService.obtenerPorUsuario(usuario.getId()));
        return "arboles/listado";
    }

    @GetMapping("/registro")
    public String mostrarFormulario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("arbol", new Arbol());
        return "arboles/registro";
    }

    @PostMapping("/registro")
    public String guardar(
            HttpSession session,
            @RequestParam String nombre,
            @RequestParam String especie,
            @RequestParam String ubicacion,
            @RequestParam String descripcion,
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam String fechaPlantacion,
            @RequestParam String estado,
            @RequestParam(required = false) String fotoUrl,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            System.err.println("❌ Usuario en sesión es NULL");
            return "redirect:/auth/login";
        }

        System.out.println("\n=== REGISTRO DE ÁRBOL ===");
        System.out.println("Usuario ID desde sesión: " + usuario.getId());
        System.out.println("Usuario Email: " + usuario.getCorreo());

        if (usuario.getId() != null) {
            Usuario usuarioRecargado = usuarioService.buscarPorId(usuario.getId());
            if (usuarioRecargado != null) {
                usuario = usuarioRecargado;
                System.out.println("✓ Usuario recargado desde BD con ID: " + usuario.getId());
            }
        }

        if (usuario.getId() == null) {
            System.err.println("El usuario en sesión no tiene ID válido");
            model.addAttribute("error", "Error: Usuario no válido. Por favor, inicia sesión nuevamente.");
            return "arboles/registro";
        }

        System.out.println("Nombre Árbol: " + nombre);

        // Crear nuevo árbol
        Arbol arbol = new Arbol();
        arbol.setNombre(nombre);
        arbol.setEspecie(especie);
        arbol.setUbicacion(ubicacion);
        arbol.setDescripcion(descripcion);
        arbol.setLatitud(latitud);
        arbol.setLongitud(longitud);
        arbol.setFechaPlantacion(LocalDate.parse(fechaPlantacion));
        arbol.setEstado(estado);
        arbol.setFotoUrl(fotoUrl);
        arbol.setUsuario(usuario);

        // Guardar en base de datos
        try {
            arbolService.guardar(arbol);
            System.out.println("Árbol registrado exitosamente");
        } catch (Exception e) {
            System.err.println("Error al guardar árbol: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al registrar el árbol: " + e.getMessage());
            return "arboles/registro";
        }

        // Redirigir al mapa de árboles
        return "redirect:/mapa/arboles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(HttpSession session, @PathVariable Long id) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        arbolService.eliminar(id);
        return "redirect:/arboles/listado";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(HttpSession session, @PathVariable Long id, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        
        Arbol arbol = arbolService.obtenerPorId(id);
        if (arbol == null) {
            return "redirect:/arboles/listado";
        }
        
        // Verificar que el árbol pertenece al usuario
        if (!arbol.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/arboles/listado";
        }
        
        model.addAttribute("arbol", arbol);
        return "arboles/editar";
    }

    // Guardar cambios de edición
    @PostMapping("/editar/{id}")
    public String guardarEdicion(
            HttpSession session,
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String especie,
            @RequestParam String ubicacion,
            @RequestParam String descripcion,
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam String fechaPlantacion,
            @RequestParam String estado,
            @RequestParam(required = false) String fotoUrl,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }

        try {
            Arbol arbolExistente = arbolService.obtenerPorId(id);
            if (arbolExistente == null) {
                return "redirect:/arboles/listado";
            }

            // Verificar propiedad
            if (!arbolExistente.getUsuario().getId().equals(usuario.getId())) {
                return "redirect:/arboles/listado";
            }

            // Actualizar campos
            arbolExistente.setNombre(nombre);
            arbolExistente.setEspecie(especie);
            arbolExistente.setUbicacion(ubicacion);
            arbolExistente.setDescripcion(descripcion);
            arbolExistente.setLatitud(latitud);
            arbolExistente.setLongitud(longitud);
            arbolExistente.setFechaPlantacion(LocalDate.parse(fechaPlantacion));
            arbolExistente.setEstado(estado);
            if (fotoUrl != null && !fotoUrl.trim().isEmpty()) {
                arbolExistente.setFotoUrl(fotoUrl);
            }

            // Guardar cambios
            arbolService.guardar(arbolExistente);
            
            System.out.println("Árbol " + id + " actualizado correctamente");
            return "redirect:/arboles/listado?success=1";
        } catch (Exception e) {
            System.err.println("Error al editar árbol: " + e.getMessage());
            model.addAttribute("error", "Error al guardar los cambios: " + e.getMessage());
            model.addAttribute("arbol", arbolService.obtenerPorId(id));
            return "arboles/editar";
        }
    }

    // Mostrar detalles de un árbol específico
    @GetMapping("/detalles/{id}")
    public String mostrarDetalles(@PathVariable Long id, Model model) {
        Arbol arbol = arbolService.obtenerPorId(id);
        if (arbol == null) {
            return "redirect:/mapa/arboles";
        }
        
        // Calcular edad estimada
        if (arbol.getFechaPlantacion() != null) {
            long dias = java.time.temporal.ChronoUnit.DAYS.between(
                arbol.getFechaPlantacion(), 
                java.time.LocalDate.now()
            );
            long anos = dias / 365;
            model.addAttribute("edadEstimada", anos);
        } else {
            model.addAttribute("edadEstimada", 0);
        }
        
        model.addAttribute("arbol", arbol);
        return "arboles/detalles-arboles";
    }

    // API REST para obtener árboles en JSON (para el mapa)
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Arbol>> obtenerArbolesAPI() {
        try {
            List<Arbol> arboles = arbolService.listarTodos();
            System.out.println("API /arboles/api - Retornando " + (arboles != null ? arboles.size() : 0) + " árboles");
            if (arboles == null) {
                arboles = new ArrayList<>();
            }
            return ResponseEntity.ok(arboles);
        } catch (Exception e) {
            System.err.println("ERROR en /arboles/api: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }
}