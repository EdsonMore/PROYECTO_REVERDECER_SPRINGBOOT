package com.example.Proyecto_Reverdecer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para recibir credenciales de login en API REST, aca lo usamos para el
 * login con JWT en ApiAuthController
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El correo es requerido")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    @NotBlank(message = "La contraseña es requerida")
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "correo='" + correo + '\'' +
                '}';
    }
}
