package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import org.springframework.stereotype.Component;


//Interceptor para proteger rutas del gestor

@Component
public class GestorAmbientalInterceptor extends BaseRoleInterceptor {

    @Override
    protected String getPathPrefix() {
        return "/gestor";
    }

    @Override
    protected boolean hasPermission(Usuario usuario) {
        return "ROLE_GESTOR_AMBIENTAL".equals(usuario.getRol());
    }
}