package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Especie;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.EspecieService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especies")
public class ApiEspecieController {

    @Autowired
    private EspecieService especieService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return usuarioService.buscarPorCorreo(auth.getName());
        }
        return null;
    }

    private boolean esAdmin(Usuario usuario) {
        return usuario != null && Boolean.TRUE.equals(usuario.getIsAdmin());
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String q) {
        try {
            List<Especie> especies;
            if (q != null && !q.trim().isEmpty()) {
                especies = especieService.buscar(q);
            } else {
                especies = especieService.listarCatalogo();
            }
            return ResponseEntity.ok(especies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<?> listarTodas() {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }
            return ResponseEntity.ok(especieService.listarActivas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return especieService.obtenerPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Especie especie) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }
            if (especie.getNombreComun() == null || especie.getNombreComun().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new LoginResponse("El nombre común es obligatorio"));
            }
            Especie guardada = especieService.guardar(especie);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Especie especie) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }
            Especie actualizada = especieService.actualizar(id, especie);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }
            especieService.desactivar(id);
            return ResponseEntity.ok(new LoginResponse("Especie desactivada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
