package com.example.Proyecto_Reverdecer.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "correo", name = "uk_usuario_correo"),
        @UniqueConstraint(columnNames = "dni", name = "uk_usuario_dni"),
        @UniqueConstraint(columnNames = "user", name = "uk_usuario_user")
})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String user;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(length = 100)
    private String apellidoMaterno;

    @Column(length = 255)
    private String direccion1;

    @Column(length = 255)
    private String direccion2;

    @Column(length = 10)
    private Integer numero;

    @Column(length = 20)
    private String genero;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_doc", length = 50)
    private TipoDoc tipoDoc;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    @Column(name = "rol", length = 50, nullable = false)
    private String rol = "ROLE_USER";

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Arbol> arboles = new ArrayList<>();

    
    public boolean esAdmin() {
        return Boolean.TRUE.equals(isAdmin) || "ROLE_ADMIN".equals(rol);
    }

    public boolean esGestorAmbiental() {
        return "ROLE_GESTOR_AMBIENTAL".equals(rol);
    }
}