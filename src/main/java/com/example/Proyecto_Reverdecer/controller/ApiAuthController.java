package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginRequest;
import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.security.JwtSecurity;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiAuthController {


//autentica usuarios
    @Autowired
    private UsuarioService usuarioService;

//clase que genera y valida tokens JWT
    @Autowired
    private JwtSecurity jwtSecurity;

    // endpoint para login y generación de token JWT
    @PostMapping("/login")
    public ResponseEntity<?> loginWithJwt(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("\n=== API LOGIN (JWT) ===");
        System.out.println("Intento de login para: " + loginRequest.getCorreo());

        try {
            // Autenticar usuario con credenciales
            Usuario usuario = usuarioService.autenticar(loginRequest.getCorreo(), loginRequest.getPassword());

            if (usuario != null) {
                System.out.println("Autenticación exitosa para: " + usuario.getCorreo());

                // Generar token JWT
                String token = jwtSecurity.generateTokenWithClaims(
                        usuario.getCorreo(),
                        usuario.getId(),
                        usuario.getRol() != null ? usuario.getRol() : "USER");

                System.out.println("Token JWT generado: " + token.substring(0, 20) + "...");

                // Retornar respuesta con token
                LoginResponse response = new LoginResponse(
                        token,
                        usuario.getUser(),
                        usuario.getId(),
                        usuario.getCorreo(),
                        usuario.getRol() != null ? usuario.getRol() : "USER");

                return ResponseEntity.ok(response);
            }

            System.out.println("Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Correo o contraseña incorrectos"));

        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error en el servidor: " + e.getMessage()));
        }
    }

    // endpoint para validar un token JWT
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        System.out.println("\n=== VALIDAR TOKEN JWT ===");

        try {
            boolean isValid = jwtSecurity.validarToken(token);

            if (isValid) {
                String username = jwtSecurity.extractUsername(token);
                System.out.println("Token válido para usuario: " + username);
                return ResponseEntity.ok(new LoginResponse("Token válido"));
            }

            System.out.println("Token inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Token inválido o expirado"));

        } catch (Exception e) {
            System.err.println("Error validando token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse("Error validando token"));
        }
    }

    // endpoint para refrescar un token JWT
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String token) {
        System.out.println("\n=== REFRESCAR TOKEN JWT ===");

        try {
            if (!jwtSecurity.validarToken(token)) {
                System.out.println("Token inválido para refrescar");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("Token inválido o expirado"));
            }

            String username = jwtSecurity.extractUsername(token);
            String userId = String.valueOf(jwtSecurity.extractClaim(token, "userId"));
            String rol = String.valueOf(jwtSecurity.extractClaim(token, "rol"));

            // Generar nuevo token
            String newToken = jwtSecurity.generateTokenWithClaims(username, Long.parseLong(userId), rol);

            System.out.println("Token refrescado para: " + username);
            return ResponseEntity.ok(new LoginResponse(
                    newToken,
                    username,
                    Long.parseLong(userId),
                    username,
                    rol));

        } catch (Exception e) {
            System.err.println("Error refrescando token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error refrescando token"));
        }
    }
}
