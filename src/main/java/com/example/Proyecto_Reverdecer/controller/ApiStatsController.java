package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.service.ArbolService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/stats")
public class ApiStatsController {

    @Autowired
    private ArbolService arbolService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public ResponseEntity<?> homeStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            long totalArboles = arbolService.listarTodos().size();
            stats.put("totalArboles", totalArboles);
        } catch (Exception e) {
            stats.put("totalArboles", 0);
        }
        try {
            long totalUsuarios = usuarioService.listarTodosAdmin().size();
            stats.put("totalUsuarios", totalUsuarios);
        } catch (Exception e) {
            stats.put("totalUsuarios", 0);
        }
        return ResponseEntity.ok(stats);
    }
}