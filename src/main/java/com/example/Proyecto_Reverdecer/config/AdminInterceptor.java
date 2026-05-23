package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import org.springframework.stereotype.Component;

/**
 * Protege las rutas y validamos que el usuario sea admin
 */
@Component
public class AdminInterceptor extends BaseRoleInterceptor {

    @Override
    protected String getPathPrefix() {
        return "/admin";
    }

    @Override
    protected boolean hasPermission(Usuario usuario) {
        return usuario.getIsAdmin() != null && usuario.getIsAdmin();
    }
}
