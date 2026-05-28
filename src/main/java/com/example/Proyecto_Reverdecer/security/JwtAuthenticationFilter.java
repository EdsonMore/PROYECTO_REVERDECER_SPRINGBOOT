package com.example.Proyecto_Reverdecer.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtSecurity jwtSecurity;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Solo para rutas de la api rest
        if (path.startsWith("/api/")) {
            try {
                String token = extractToken(request);

                if (token != null && jwtSecurity.validarToken(token)) {
                    String username = jwtSecurity.extractUsername(token);

                    if (username != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username, null, new ArrayList<>());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("JWT válido para: " + username);
                    }
                }

            } catch (Exception e) {
                System.err.println("Error JWT: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}