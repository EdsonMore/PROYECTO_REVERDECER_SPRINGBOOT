package com.example.Proyecto_Reverdecer.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtSecurity {

    // Clave fija para firmar tokens
    private final SecretKey key = Keys.hmacShaKeyFor("Leyva-expondrá-1234567890-PROYECTO-REVERDECER".getBytes());

    // Genera token solo con email
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(key)
                .compact();
    }

    // Genera token con datos extra (userId y rol)
    public String generateTokenWithClaims(String username, Long userId, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    // Verifica si el token es válido
    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.err.println("Error JWT: " + e.getMessage());
            return false;
        }
    }

    // Obtiene el email del token
    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            System.err.println("Error al extraer username del token: " + e.getMessage());
            return null;
        }
    }

    // Extrae cualquier campo del token
    public Object extractClaim(String token, String claimName) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(claimName);
        } catch (Exception e) {
            System.err.println("Error al extraer claim '" + claimName + "': " + e.getMessage());
            return null;
        }
    }
}