package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Alerta;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.AlertaService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
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
@RequestMapping("/api/alertas")
public class ApiAlertasController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AlertaService alertaService;

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return usuarioService.buscarPorCorreo(auth.getName());
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<?> listarAlertas() {
        try {
            List<Alerta> alertas = alertaService.listarTodas();
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/activas")
    public ResponseEntity<?> listarActivas() {
        try {
            List<Alerta> alertas = alertaService.listarActivas();
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAlerta(@PathVariable Long id) {
        try {
            Alerta alerta = alertaService.obtenerPorId(id).orElse(null);
            if (alerta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Alerta no encontrada"));
            }
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crearAlerta(@RequestBody Alerta alerta) {
        try {
            Alerta creada = alertaService.crear(alerta);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/leida")
    public ResponseEntity<?> marcarLeida(@PathVariable Long id) {
        try {
            Alerta alerta = alertaService.marcarLeida(id);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/resolver")
    public ResponseEntity<?> resolverAlerta(@PathVariable Long id) {
        try {
            Alerta alerta = alertaService.resolver(id);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAlerta(@PathVariable Long id) {
        try {
            alertaService.eliminar(id);
            return ResponseEntity.ok(new LoginResponse("Alerta eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<?> resumenAlertas() {
        try {
            Map<String, Object> resumen = new HashMap<>();
            resumen.put("activas", alertaService.contarActivas());
            resumen.put("criticas", alertaService.contarPorNivel("CRITICA"));
            resumen.put("altas", alertaService.contarPorNivel("ALTA"));
            resumen.put("medias", alertaService.contarPorNivel("MEDIA"));
            resumen.put("bajas", alertaService.contarPorNivel("BAJA"));
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
