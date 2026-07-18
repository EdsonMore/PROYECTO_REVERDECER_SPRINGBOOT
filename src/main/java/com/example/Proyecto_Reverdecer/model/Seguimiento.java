package com.example.Proyecto_Reverdecer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "seguimientos")
public class Seguimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arbol_id", nullable = false)
    private Arbol arbol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "altura_cm")
    private Double alturaCm;

    @Column(length = 20)
    private String salud;

    @Column(name = "tipo_seguimiento", length = 30)
    private String tipoSeguimiento;

    @Column(name = "fecha_seguimiento", nullable = false)
    private LocalDate fechaSeguimiento;

    @Column(name = "temperatura_ambiente")
    private Double temperaturaAmbiente;

    @Column(name = "humedad_suelo")
    private Double humedadSuelo;

    @Column(name = "notas_tecnicas", length = 1000)
    private String notasTecnicas;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
