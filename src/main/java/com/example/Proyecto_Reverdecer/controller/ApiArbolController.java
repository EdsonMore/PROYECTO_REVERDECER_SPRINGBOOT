package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Arbol;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.ArbolService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/arboles")
public class ApiArbolController {

    @Autowired
    private ArbolService arbolService;

    @Autowired
    private UsuarioService usuarioService;

    private void limpiarPassword(Arbol arbol) {
        if (arbol != null && arbol.getUsuario() != null) {
            arbol.getUsuario().setPassword(null);
        }
    }

    private void limpiarPassword(List<Arbol> arboles) {
        if (arboles != null) {
            for (Arbol a : arboles) {
                limpiarPassword(a);
            }
        }
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return usuarioService.buscarPorCorreo(auth.getName());
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Arbol> arboles = arbolService.listarTodos();
            limpiarPassword(arboles);
            return ResponseEntity.ok(arboles);
        } catch (Exception e) {
            System.err.println("Error al listar árboles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/mis-arboles")
    public ResponseEntity<?> listarMisArboles() {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            List<Arbol> arboles = arbolService.obtenerPorUsuario(usuario.getId());
            limpiarPassword(arboles);
            return ResponseEntity.ok(arboles);
        } catch (Exception e) {
            System.err.println("Error al listar mis árboles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Arbol arbol = arbolService.obtenerPorId(id);
            if (arbol == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Árbol no encontrado"));
            }

            limpiarPassword(arbol);
            return ResponseEntity.ok(arbol);
        } catch (Exception e) {
            System.err.println("Error al obtener árbol: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Arbol arbol) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            if (arbol.getEspecie() == null || arbol.getEspecie().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new LoginResponse("La especie es obligatoria"));
            }
            if (arbol.getUbicacion() == null || arbol.getUbicacion().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new LoginResponse("La ubicación es obligatoria"));
            }
            if (arbol.getEstado() == null || arbol.getEstado().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new LoginResponse("El estado es obligatorio"));
            }

            arbol.setUsuario(usuario);
            if (arbol.getFechaPlantacion() == null) {
                arbol.setFechaPlantacion(LocalDate.now());
            }
            arbol.setFechaRegistro(LocalDate.now());

            Arbol guardado = arbolService.guardar(arbol);
            limpiarPassword(guardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            System.err.println("Error al crear árbol: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Arbol arbol) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            Arbol existente = arbolService.obtenerPorId(id);
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Árbol no encontrado"));
            }

            if (!existente.getUsuario().getId().equals(usuario.getId()) && !Boolean.TRUE.equals(usuario.getIsAdmin())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("No tienes permiso para editar este árbol"));
            }

            if (arbol.getNombre() != null) existente.setNombre(arbol.getNombre());
            if (arbol.getEspecie() != null) existente.setEspecie(arbol.getEspecie());
            if (arbol.getUbicacion() != null) existente.setUbicacion(arbol.getUbicacion());
            if (arbol.getDescripcion() != null) existente.setDescripcion(arbol.getDescripcion());
            if (arbol.getLatitud() != null) existente.setLatitud(arbol.getLatitud());
            if (arbol.getLongitud() != null) existente.setLongitud(arbol.getLongitud());
            if (arbol.getFechaPlantacion() != null) existente.setFechaPlantacion(arbol.getFechaPlantacion());
            if (arbol.getEstado() != null) existente.setEstado(arbol.getEstado());
            if (arbol.getFotoUrl() != null) existente.setFotoUrl(arbol.getFotoUrl());

            Arbol actualizado = arbolService.guardar(existente);
            limpiarPassword(actualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            System.err.println("Error al actualizar árbol: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            Arbol existente = arbolService.obtenerPorId(id);
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Árbol no encontrado"));
            }

            if (!existente.getUsuario().getId().equals(usuario.getId()) && !Boolean.TRUE.equals(usuario.getIsAdmin())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("No tienes permiso para eliminar este árbol"));
            }

            arbolService.eliminar(id);
            return ResponseEntity.ok(new LoginResponse("Árbol eliminado correctamente"));
        } catch (Exception e) {
            System.err.println("Error al eliminar árbol: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
