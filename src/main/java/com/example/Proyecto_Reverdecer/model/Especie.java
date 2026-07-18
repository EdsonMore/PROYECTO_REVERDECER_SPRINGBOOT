package com.example.Proyecto_Reverdecer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "especies_catalog")
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_comun", nullable = false, length = 150)
    private String nombreComun;

    @Column(name = "nombre_cientifico", nullable = false, length = 200)
    private String nombreCientifico;

    @Column(length = 100)
    private String familia;

    @Column(length = 100)
    private String origen;

    @Column(length = 100)
    private String clima;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "altura_maxima_cm")
    private Integer alturaMaximaCm;

    @Column(length = 30)
    private String crecimiento;

    @Column(name = "requiere_agua", length = 30)
    private String requiereAgua;

    @Column(name = "tolerancia_sequia", length = 30)
    private String toleranciaSequia;

    @Column(name = "uso_principal", length = 100)
    private String usoPrincipal;

    @Column(name = "temp_min")
    private Double tempMin;

    @Column(name = "temp_max")
    private Double tempMax;

    @Column(name = "probabilidad_supervivencia")
    private Integer probabilidadSupervivencia;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "auto_registrada")
    private Boolean autoRegistrada = false;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
