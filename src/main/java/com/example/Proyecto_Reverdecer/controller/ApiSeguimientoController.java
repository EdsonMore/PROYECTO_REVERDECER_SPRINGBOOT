package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Seguimiento;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.SeguimientoService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
public class ApiSeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return usuarioService.buscarPorCorreo(auth.getName());
        }
        return null;
    }

    private void limpiarPassword(Object obj) {
        if (obj instanceof Seguimiento s) {
            if (s.getUsuario() != null) s.getUsuario().setPassword(null);
            if (s.getArbol() != null && s.getArbol().getUsuario() != null) {
                s.getArbol().getUsuario().setPassword(null);
            }
        }
    }

    private void limpiarPassword(List<Seguimiento> lista) {
        if (lista != null) lista.forEach(this::limpiarPassword);
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) Long arbolId) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            List<Seguimiento> resultados;
            if (arbolId != null) {
                resultados = seguimientoService.listarPorArbol(arbolId);
            } else {
                resultados = seguimientoService.listarPorUsuario(usuario.getId());
            }

            limpiarPassword(resultados);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            System.err.println("Error al listar seguimientos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Seguimiento seguimiento = seguimientoService.obtenerPorId(id);
            if (seguimiento == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Seguimiento no encontrado"));
            }

            limpiarPassword(seguimiento);
            return ResponseEntity.ok(seguimiento);
        } catch (Exception e) {
            System.err.println("Error al obtener seguimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/publico/arbol/{arbolId}")
    public ResponseEntity<?> listarPorArbolPublico(@PathVariable Long arbolId) {
        try {
            List<Seguimiento> resultados = seguimientoService.listarPorArbol(arbolId);
            limpiarPassword(resultados);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            System.err.println("Error al listar seguimientos públicos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/arbol/{arbolId}")
    public ResponseEntity<?> listarPorArbol(@PathVariable Long arbolId) {
        try {
            List<Seguimiento> resultados = seguimientoService.listarPorArbol(arbolId);
            limpiarPassword(resultados);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            System.err.println("Error al listar seguimientos por árbol: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Seguimiento seguimiento) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            if (seguimiento.getTitulo() == null || seguimiento.getTitulo().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse("El título es obligatorio"));
            }

            if (seguimiento.getArbol() == null || seguimiento.getArbol().getId() == null) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse("El árbol es obligatorio"));
            }

            Seguimiento guardado = seguimientoService.crear(seguimiento, usuario.getId());
            limpiarPassword(guardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            System.err.println("Error al crear seguimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Seguimiento seguimiento) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            Seguimiento actualizado = seguimientoService.actualizar(id, seguimiento, usuario.getId());
            if (actualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Seguimiento no encontrado o sin permisos"));
            }

            limpiarPassword(actualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            System.err.println("Error al actualizar seguimiento: " + e.getMessage());
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

            boolean eliminado = seguimientoService.eliminar(id, usuario.getId());
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Seguimiento no encontrado o sin permisos"));
            }

            return ResponseEntity.ok(new LoginResponse("Seguimiento eliminado correctamente"));
        } catch (Exception e) {
            System.err.println("Error al eliminar seguimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<?> resumen(@RequestParam(required = false) Long arbolId) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            long total;
            if (arbolId != null) {
                total = seguimientoService.contarPorArbol(arbolId);
            } else {
                total = seguimientoService.contarPorUsuario(usuario.getId());
            }

            return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
                put("total", total);
            }});
        } catch (Exception e) {
            System.err.println("Error al obtener resumen de seguimientos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
