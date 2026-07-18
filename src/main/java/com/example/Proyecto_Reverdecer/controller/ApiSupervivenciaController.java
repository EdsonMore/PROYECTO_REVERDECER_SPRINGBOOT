package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.model.Especie;
import com.example.Proyecto_Reverdecer.service.EspecieService;
import com.example.Proyecto_Reverdecer.service.SupervivenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/supervivencia")
public class ApiSupervivenciaController {

    @Autowired
    private SupervivenciaService supervivenciaService;

    @Autowired
    private EspecieService especieService;

    @GetMapping("/calcular")
    public ResponseEntity<?> calcularSupervivencia(
            @RequestParam(required = false) Long especieId,
            @RequestParam(required = false) String especieNombre,
            @RequestParam double lat,
            @RequestParam double lng) {

        Especie especie = null;
        if (especieId != null) {
            Optional<Especie> opt = especieService.obtenerPorId(especieId);
            if (opt.isPresent()) {
                especie = opt.get();
            }
        }

        if (especie == null && especieNombre != null && !especieNombre.trim().isEmpty()) {
            Optional<Especie> opt = especieService.buscarPorNombreCientifico(especieNombre.trim());
            if (opt.isEmpty()) {
                opt = especieService.buscarPorNombreComun(especieNombre.trim());
            }
            if (opt.isPresent()) {
                especie = opt.get();
            }
        }

        var resultado = supervivenciaService.calcularSupervivencia(
                especieId, especie, lat, lng);

        if (resultado.containsKey("error")) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
}