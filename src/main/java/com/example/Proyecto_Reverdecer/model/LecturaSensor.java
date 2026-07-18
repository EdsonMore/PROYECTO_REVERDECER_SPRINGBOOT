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
@Table(name = "lecturas_sensor")
public class LecturaSensor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valor;

    @Column(length = 20)
    private String unidad;

    private Double humedad;

    private Double temperatura;

    @Column(name = "fecha_lectura", nullable = false)
    private LocalDateTime fechaLectura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispositivo_id", nullable = false)
    private DispositivoIoT dispositivo;
}
