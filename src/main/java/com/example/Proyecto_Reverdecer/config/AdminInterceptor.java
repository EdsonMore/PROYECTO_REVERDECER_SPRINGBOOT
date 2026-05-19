package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        // Solo verificar si la ruta comienza con /admin
        if (!request.getRequestURI().startsWith("/admin")) {
            return true;
        }

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Verificar que exista usuario en sesión
        if (usuario == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        // Verificar que sea admin
        if (usuario.getIsAdmin() == null || !usuario.getIsAdmin()) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }

        return true;
    }
}
