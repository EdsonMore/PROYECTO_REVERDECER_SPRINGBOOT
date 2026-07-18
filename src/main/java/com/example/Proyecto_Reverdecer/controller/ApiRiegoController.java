package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Riego;
import com.example.Proyecto_Reverdecer.service.RiegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/riego")
public class ApiRiegoController {

    @Autowired
    private RiegoService riegoService;

    @GetMapping
    public ResponseEntity<?> listarRiegos() {
        try {
            List<Riego> riegos = riegoService.listarTodos();
            return ResponseEntity.ok(riegos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRiego(@PathVariable Long id) {
        try {
            Riego riego = riegoService.obtenerPorId(id).orElse(null);
            if (riego == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Riego no encontrado"));
            }
            return ResponseEntity.ok(riego);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/dispositivo/{dispositivoId}")
    public ResponseEntity<?> listarPorDispositivo(@PathVariable Long dispositivoId) {
        try {
            List<Riego> riegos = riegoService.listarPorDispositivo(dispositivoId);
            return ResponseEntity.ok(riegos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> programarRiego(@RequestBody Riego riego) {
        try {
            Riego programado = riegoService.programar(riego);
            return ResponseEntity.status(HttpStatus.CREATED).body(programado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/ejecutar")
    public ResponseEntity<?> ejecutarRiego(@PathVariable Long id) {
        try {
            Riego ejecutado = riegoService.ejecutar(id);
            return ResponseEntity.ok(ejecutado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarRiego(@PathVariable Long id) {
        try {
            Riego cancelado = riegoService.cancelar(id);
            return ResponseEntity.ok(cancelado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRiego(@PathVariable Long id) {
        try {
            riegoService.eliminar(id);
            return ResponseEntity.ok(new LoginResponse("Riego eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<?> resumenRiego() {
        try {
            Map<String, Object> resumen = new HashMap<>();
            resumen.put("programados", riegoService.contarPorEstado("PROGRAMADO"));
            resumen.put("ejecutados", riegoService.contarPorEstado("EJECUTADO"));
            resumen.put("cancelados", riegoService.contarPorEstado("CANCELADO"));
            resumen.put("total", riegoService.listarTodos().size());
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
