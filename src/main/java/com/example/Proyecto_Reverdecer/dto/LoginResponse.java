package com.example.Proyecto_Reverdecer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para respuesta de login con JWT, para retorna el token y datos del
 * usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String username;
    private Long userId;
    private String correo;
    private String rol;
    private String mensaje;

    public LoginResponse(String token, String username, Long userId, String correo, String rol) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.correo = correo;
        this.rol = rol;
    }

    public LoginResponse(String mensaje) {
        this.mensaje = mensaje;
    }
}
