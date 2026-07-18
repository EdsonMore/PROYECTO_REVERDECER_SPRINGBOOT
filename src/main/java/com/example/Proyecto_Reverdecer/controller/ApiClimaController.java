package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.service.ClimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/clima")
public class ApiClimaController {

    @Autowired
    private ClimaService climaService;

    @GetMapping
    public ResponseEntity<?> obtenerClima(@RequestParam double lat, @RequestParam double lng) {
        Map<String, Object> resultado = climaService.obtenerClima(lat, lng);
        if (resultado.containsKey("error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }
}
