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

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ApiAdminController {

    @Autowired
    private UsuarioService usuarioService;

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

    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios(@RequestParam(required = false) String search) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }

            List<Usuario> usuarios;
            if (search != null && !search.trim().isEmpty()) {
                usuarios = usuarioService.buscarPorNombre(search.trim());
            } else {
                usuarios = usuarioService.listarTodosAdmin();
            }

            for (Usuario u : usuarios) {
                u.setPassword(null);
            }

            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }

            Usuario usuario = usuarioService.buscarPorId(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Usuario no encontrado"));
            }

            usuario.setPassword(null);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }

            if (usuarioService.registrar(usuario)) {
                Usuario creado = usuarioService.buscarPorCorreo(usuario.getCorreo());
                if (creado != null) {
                    creado.setPassword(null);
                    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
                }
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse("Error al crear usuario. Verifica los datos."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }

            Usuario existente = usuarioService.buscarPorId(id);
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginResponse("Usuario no encontrado"));
            }

            if (usuario.getNombres() != null) existente.setNombres(usuario.getNombres());
            if (usuario.getApellidoPaterno() != null) existente.setApellidoPaterno(usuario.getApellidoPaterno());
            if (usuario.getApellidoMaterno() != null) existente.setApellidoMaterno(usuario.getApellidoMaterno());
            if (usuario.getCorreo() != null) existente.setCorreo(usuario.getCorreo());
            if (usuario.getDni() != null) existente.setDni(usuario.getDni());
            if (usuario.getRol() != null) existente.setRol(usuario.getRol());
            if (usuario.getIsAdmin() != null) existente.setIsAdmin(usuario.getIsAdmin());
            if (usuario.getActivo() != null) existente.setActivo(usuario.getActivo());
            if (usuario.getGenero() != null) existente.setGenero(usuario.getGenero());
            if (usuario.getNumero() != null) existente.setNumero(usuario.getNumero());
            if (usuario.getDireccion1() != null) existente.setDireccion1(usuario.getDireccion1());
            if (usuario.getDireccion2() != null) existente.setDireccion2(usuario.getDireccion2());
            if (usuario.getTipoDoc() != null) existente.setTipoDoc(usuario.getTipoDoc());
            if (usuario.getFechaNacimiento() != null) existente.setFechaNacimiento(usuario.getFechaNacimiento());
            if (usuario.getUser() != null) existente.setUser(usuario.getUser());

            if (usuarioService.actualizar(existente)) {
                Usuario actualizado = usuarioService.buscarPorId(id);
                actualizado.setPassword(null);
                return ResponseEntity.ok(actualizado);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse("Error al actualizar usuario"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long id) {
        try {
            Usuario admin = obtenerUsuarioAutenticado();
            if (!esAdmin(admin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new LoginResponse("Acceso denegado"));
            }

            if (admin.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new LoginResponse("No puedes desactivar tu propia cuenta"));
            }

            if (usuarioService.desactivar(id)) {
                return ResponseEntity.ok(new LoginResponse("Usuario desactivado correctamente"));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginResponse("Usuario no encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error: " + e.getMessage()));
        }
    }
}
