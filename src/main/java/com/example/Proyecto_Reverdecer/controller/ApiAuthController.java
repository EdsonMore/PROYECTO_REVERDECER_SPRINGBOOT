package com.example.Proyecto_Reverdecer.controller;

import com.example.Proyecto_Reverdecer.dto.LoginRequest;
import com.example.Proyecto_Reverdecer.dto.LoginResponse;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.security.JwtSecurity;
import com.example.Proyecto_Reverdecer.service.AuditoriaAccesoService;
import com.example.Proyecto_Reverdecer.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private AuditoriaAccesoService auditoriaService;

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // endpoint para login y generación de token JWT
    @PostMapping("/login")
    public ResponseEntity<?> loginWithJwt(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        System.out.println("\n=== API LOGIN (JWT) ===");
        System.out.println("Intento de login para: " + loginRequest.getCorreo());

        String clientIp = getClientIp(request);

        try {
            // Autenticar usuario con credenciales
            Usuario usuario = usuarioService.autenticar(loginRequest.getCorreo(), loginRequest.getPassword());

            if (usuario != null) {
                System.out.println("Autenticación exitosa para: " + usuario.getCorreo());

                // Registrar auditoría de login exitoso
                auditoriaService.registrar(
                        "LOGIN_EXITOSO",
                        "Inicio de sesión exitoso: " + loginRequest.getCorreo(),
                        clientIp,
                        "/api/auth/login",
                        usuario.getId()
                );

                // Generar token JWT
                String token = jwtSecurity.generateTokenWithClaims(
                        usuario.getCorreo(),
                        usuario.getId(),
                        usuario.getRol() != null ? usuario.getRol() : "USER",
                        usuario.getIsAdmin());

                System.out.println("Token JWT generado: " + token.substring(0, 20) + "...");

                // Retornar respuesta con token
                LoginResponse response = new LoginResponse(
                        token,
                        usuario.getUser(),
                        usuario.getId(),
                        usuario.getCorreo(),
                        usuario.getRol() != null ? usuario.getRol() : "USER",
                        usuario.getIsAdmin());

                return ResponseEntity.ok(response);
            }

            System.out.println("Credenciales inválidas");

            // Registrar auditoría de login fallido
            auditoriaService.registrarSinUsuario(
                    "LOGIN_FALLIDO",
                    "Intento de inicio de sesión fallido: " + loginRequest.getCorreo(),
                    clientIp,
                    "/api/auth/login"
            );

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Correo o contraseña incorrectos"));

        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();

            // Registrar auditoría de error en login
            auditoriaService.registrarSinUsuario(
                    "LOGIN_FALLIDO",
                    "Error en inicio de sesión: " + e.getMessage(),
                    clientIp,
                    "/api/auth/login"
            );

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
            Object isAdminClaim = jwtSecurity.extractClaim(token, "isAdmin");
            boolean isAdmin = isAdminClaim instanceof Boolean && (Boolean) isAdminClaim;

            // Generar nuevo token
            String newToken = jwtSecurity.generateTokenWithClaims(username, Long.parseLong(userId), rol, isAdmin);

            System.out.println("Token refrescado para: " + username);
            return ResponseEntity.ok(new LoginResponse(
                    newToken,
                    username,
                    Long.parseLong(userId),
                    username,
                    rol,
                    isAdmin));

        } catch (Exception e) {
            System.err.println("Error refrescando token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Error refrescando token"));
        }
    }
}
