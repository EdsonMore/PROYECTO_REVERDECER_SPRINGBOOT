package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiPerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> obtenerPerfil() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorCorreo(email);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Usuario no encontrado"));
            }

            usuario.setPassword(null);

            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            System.err.println("Error al obtener perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> actualizarPerfil(@RequestBody Usuario usuarioActualizado) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("No autenticado"));
            }

            String email = authentication.getName();
            Usuario usuarioExistente = usuarioService.buscarPorCorreo(email);

            if (usuarioExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Usuario no encontrado"));
            }

            if (usuarioActualizado.getNombres() != null)
                usuarioExistente.setNombres(usuarioActualizado.getNombres());
            if (usuarioActualizado.getApellidoPaterno() != null)
                usuarioExistente.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
            if (usuarioActualizado.getApellidoMaterno() != null)
                usuarioExistente.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
            if (usuarioActualizado.getCorreo() != null)
                usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
            if (usuarioActualizado.getGenero() != null)
                usuarioExistente.setGenero(usuarioActualizado.getGenero());
            if (usuarioActualizado.getNumero() != null)
                usuarioExistente.setNumero(usuarioActualizado.getNumero());
            if (usuarioActualizado.getDireccion1() != null)
                usuarioExistente.setDireccion1(usuarioActualizado.getDireccion1());
            if (usuarioActualizado.getDireccion2() != null)
                usuarioExistente.setDireccion2(usuarioActualizado.getDireccion2());

            if (usuarioService.actualizar(usuarioExistente)) {
                Usuario respuesta = usuarioService.buscarPorId(usuarioExistente.getId());
                respuesta.setPassword(null);
                return ResponseEntity.ok(respuesta);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse("Error al actualizar perfil"));
        } catch (Exception e) {
            System.err.println("Error al actualizar perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
