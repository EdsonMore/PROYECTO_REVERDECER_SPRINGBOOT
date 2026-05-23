package com.example.Proyecto_Reverdecer.dto;

/**
 * DTO para respuesta de login con JWT, para retorna el token y datos del
 * usuario
 */
public class LoginResponse {

    private String token;
    private String username;
    private Long userId;
    private String correo;
    private String rol;
    private String mensaje;

    // Constructores
    public LoginResponse() {
    }

    public LoginResponse(String token, String username, Long userId, String correo, String rol) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.correo = correo;
        this.rol = rol;
        this.mensaje = "Login exitoso";
    }

    public LoginResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
