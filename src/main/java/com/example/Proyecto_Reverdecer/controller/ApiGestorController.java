package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.GestorAmbientalService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gestor")
public class ApiGestorController {

    @Autowired
    private GestorAmbientalService gestorService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return usuarioService.buscarPorCorreo(auth.getName());
        }
        return null;
    }

    @GetMapping("/resumen-dashboard")
    public ResponseEntity<?> resumenDashboard() {
        try {
            Map<String, Object> resumen = gestorService.resumenDashboard();
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/arboles-por-estado")
    public ResponseEntity<?> arbolesPorEstado() {
        try {
            Map<String, Long> data = gestorService.arbolesPorEstado();
            List<Map<String, Object>> resultado = data.entrySet().stream()
                    .map(e -> {
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("estado", e.getKey());
                        item.put("cantidad", e.getValue());
                        return item;
                    })
                    .toList();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/alertas-por-zona")
    public ResponseEntity<?> alertasPorZona() {
        try {
            Map<String, Long> data = gestorService.alertasPorZona();
            List<Map<String, Object>> resultado = data.entrySet().stream()
                    .map(e -> {
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("zona", e.getKey());
                        item.put("alertas", e.getValue());
                        return item;
                    })
                    .toList();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/zonas-criticas")
    public ResponseEntity<?> zonasCriticas() {
        try {
            Map<String, Long> data = gestorService.zonasCriticas();
            List<Map<String, Object>> resultado = data.entrySet().stream()
                    .map(e -> {
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("zona", e.getKey());
                        item.put("cantidad", e.getValue());
                        return item;
                    })
                    .toList();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> estadisticas() {
        try {
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("totalArboles", gestorService.totalArboles());
            stats.put("arbolesPorEspecie", gestorService.arbolesPorEspecie());
            stats.put("arbolesPorZona", gestorService.arbolesPorZona());
            stats.put("arbolesPorEstado", gestorService.arbolesPorEstado());
            stats.put("porcentajeRiesgo", gestorService.porcentajeEnRiesgo());
            stats.put("especiesActivas", gestorService.especiesActivasPorConteo());
            stats.put("mejorSupervivencia", gestorService.especiesConMejorSupervivencia());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
