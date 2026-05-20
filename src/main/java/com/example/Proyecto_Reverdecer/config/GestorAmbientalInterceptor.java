package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class GestorAmbientalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!request.getRequestURI().startsWith("/gestor")) {
            return true;
        }

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        // Verificar rol ROLE_GESTOR_AMBIENTAL
        boolean esGestor = "ROLE_GESTOR_AMBIENTAL".equals(usuario.getRol());

        if (!esGestor) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }

        return true;
    }
}