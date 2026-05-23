package com.example.Proyecto_Reverdecer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para recibir credenciales de login en API REST, aca lo usamos para el
 * login con JWT en ApiAuthController
 */
public class LoginRequest {

    @NotBlank(message = "El correo es requerido")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    @NotBlank(message = "La contraseña es requerida")
    private String password;

    // Constructores
    public LoginRequest() {
    }

    public LoginRequest(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "correo='" + correo + '\'' +
                '}';
    }
}
