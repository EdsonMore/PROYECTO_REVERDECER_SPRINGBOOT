package com.example.Proyecto_Reverdecer.model;

import java.time.LocalDate;
import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "arboles")
public class Arbol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String especie;

    @Column(nullable = false, length = 255)
    private String ubicacion;

    @Column(name = "fecha_plantacion", nullable = false)
    private LocalDate fechaPlantacion;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "latitud", nullable = true)
    private Double latitud;

    @Column(name = "longitud", nullable = true)
    private Double longitud;

    @Column(name = "nombre", length = 100, nullable = true)
    private String nombre;

    @Column(name = "foto_url", length = 500, nullable = true)
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}