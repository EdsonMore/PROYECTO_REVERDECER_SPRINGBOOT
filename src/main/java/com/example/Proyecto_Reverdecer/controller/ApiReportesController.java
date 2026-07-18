package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.service.ArbolService;
import com.example.Proyecto_Reverdecer.service.GestorAmbientalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/reportes")
public class ApiReportesController {

    @Autowired
    private ArbolService arbolService;

    @Autowired
    private GestorAmbientalService gestorService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboardReportes() {
        try {
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("totalArboles", arbolService.listarTodos().size());
            reporte.put("arbolesPorEstado", gestorService.arbolesPorEstado());
            reporte.put("arbolesPorEspecie", gestorService.arbolesPorEspecie());
            reporte.put("arbolesPorZona", gestorService.arbolesPorZona());
            reporte.put("porcentajeRiesgo", gestorService.porcentajeEnRiesgo());
            reporte.put("especiesActivas", gestorService.especiesActivasPorConteo());
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/estado-arbolado")
    public ResponseEntity<?> reportePorEstado() {
        try {
            return ResponseEntity.ok(gestorService.arbolesPorEstado());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/especies")
    public ResponseEntity<?> reportePorEspecie() {
        try {
            return ResponseEntity.ok(gestorService.arbolesPorEspecie());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/zonas")
    public ResponseEntity<?> reportePorZona() {
        try {
            return ResponseEntity.ok(gestorService.arbolesPorZona());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/zonas-criticas")
    public ResponseEntity<?> reporteZonasCriticas() {
        try {
            return ResponseEntity.ok(gestorService.zonasCriticas());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/supervivencia")
    public ResponseEntity<?> reporteSupervivencia() {
        try {
            return ResponseEntity.ok(gestorService.especiesConMejorSupervivencia());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/alertas-zona")
    public ResponseEntity<?> reporteAlertasPorZona() {
        try {
            return ResponseEntity.ok(gestorService.alertasPorZona());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
