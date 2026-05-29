package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

// Aca tenemso la lógica común para validar roles y proteger rutas.
 
public abstract class BaseRoleInterceptor implements HandlerInterceptor {

    protected abstract String getPathPrefix();

    protected abstract boolean hasPermission(Usuario usuario);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // verificar si la ruta actual coincide con el prefijo del interceptor
        if (!request.getRequestURI().startsWith(getPathPrefix())) {
            return true;
        }

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Verificar que exista usuario en sesión
        if (usuario == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        // Verificar permisos específicos del rol
        if (!hasPermission(usuario)) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }

        return true;
    }
}
