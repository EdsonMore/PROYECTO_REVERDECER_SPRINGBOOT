package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.AuditoriaAcceso;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.AuditoriaAccesoService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auditoria")
public class ApiAuditoriaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuditoriaAccesoService auditoriaService;

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
    public ResponseEntity<?> listarAuditoria() {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }
            List<AuditoriaAcceso> logs = auditoriaService.listarTodas();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }
            List<AuditoriaAcceso> logs = auditoriaService.listarPorUsuario(usuarioId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/recientes")
    public ResponseEntity<?> listarRecientes() {
        try {
            List<AuditoriaAcceso> logs = auditoriaService.listarRecientes();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarAccion(
            @RequestParam String accion,
            @RequestParam(required = false) String detalle,
            @RequestParam(required = false) String endpoint,
            HttpServletRequest request) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }
            AuditoriaAcceso log = auditoriaService.registrar(
                accion, detalle, request.getRemoteAddr(), endpoint, usuario.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(log);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<?> resumenAuditoria() {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }
            Map<String, Object> resumen = new HashMap<>();
            resumen.put("total", auditoriaService.listarTodas().size());
            resumen.put("loginExitosos", auditoriaService.contarPorAccion("LOGIN_EXITOSO"));
            resumen.put("loginFallidos", auditoriaService.contarPorAccion("LOGIN_FALLIDO"));
            resumen.put("accesosDenegados", auditoriaService.contarPorAccion("ACCESO_DENEGADO"));
            resumen.put("recientes7dias", auditoriaService.listarRecientes().size());
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
