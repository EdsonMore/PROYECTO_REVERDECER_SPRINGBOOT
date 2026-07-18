package com.example.Proyecto_Reverdecer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "riegos")
public class Riego implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean activado = false;

    @Column(name = "umbral_humedad")
    private Double umbralHumedad;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "fecha_programado")
    private LocalDateTime fechaProgramado;

    @Column(name = "fecha_ejecutado")
    private LocalDateTime fechaEjecutado;

    @Column(length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispositivo_id")
    private DispositivoIoT dispositivo;
}
