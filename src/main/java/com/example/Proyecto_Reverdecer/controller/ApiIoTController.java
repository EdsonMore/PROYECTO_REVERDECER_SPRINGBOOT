package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.DispositivoIoT;
import com.example.Proyecto_Reverdecer.model.LecturaSensor;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.DispositivoIoTService;
import com.example.Proyecto_Reverdecer.service.LecturaSensorService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/iot")
public class ApiIoTController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DispositivoIoTService dispositivoService;

    @Autowired
    private LecturaSensorService lecturaService;

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return usuarioService.buscarPorCorreo(auth.getName());
        }
        return null;
    }

    @GetMapping("/dispositivos")
    public ResponseEntity<?> listarDispositivos() {
        try {
            List<DispositivoIoT> dispositivos = dispositivoService.listarTodos();
            return ResponseEntity.ok(dispositivos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/dispositivos/activos")
    public ResponseEntity<?> listarActivos() {
        try {
            List<DispositivoIoT> dispositivos = dispositivoService.listarActivos();
            return ResponseEntity.ok(dispositivos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/dispositivos/{id}")
    public ResponseEntity<?> obtenerDispositivo(@PathVariable Long id) {
        try {
            DispositivoIoT dispositivo = dispositivoService.obtenerPorId(id).orElse(null);
            if (dispositivo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Dispositivo no encontrado"));
            }
            return ResponseEntity.ok(dispositivo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/dispositivos")
    public ResponseEntity<?> crearDispositivo(@RequestBody DispositivoIoT dispositivo) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario != null) {
                dispositivo.setUsuario(usuario);
            }
            DispositivoIoT creado = dispositivoService.guardar(dispositivo);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/dispositivos/{id}")
    public ResponseEntity<?> actualizarDispositivo(@PathVariable Long id, @RequestBody DispositivoIoT dispositivo) {
        try {
            DispositivoIoT actualizado = dispositivoService.actualizar(id, dispositivo);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/dispositivos/{id}")
    public ResponseEntity<?> eliminarDispositivo(@PathVariable Long id) {
        try {
            dispositivoService.eliminar(id);
            return ResponseEntity.ok(new LoginResponse("Dispositivo eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/dispositivos/{id}/lecturas")
    public ResponseEntity<?> listarLecturas(@PathVariable Long id) {
        try {
            List<LecturaSensor> lecturas = lecturaService.listarPorDispositivo(id);
            return ResponseEntity.ok(lecturas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/lecturas")
    public ResponseEntity<?> registrarLectura(@RequestBody LecturaSensor lectura) {
        try {
            LecturaSensor guardada = lecturaService.guardar(lectura);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/lecturas/recientes")
    public ResponseEntity<?> lecturasRecientes() {
        try {
            List<LecturaSensor> lecturas = lecturaService.obtenerLecturasRecientes();
            return ResponseEntity.ok(lecturas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<?> resumenIoT() {
        try {
            Map<String, Object> resumen = new HashMap<>();
            resumen.put("totalDispositivos", dispositivoService.contarActivos());
            resumen.put("lecturasRecientes", lecturaService.obtenerLecturasRecientes().size());
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
