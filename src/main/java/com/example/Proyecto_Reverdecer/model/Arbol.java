package com.example.Proyecto_Reverdecer.model;

import java.time.LocalDate;

public class Arbol {

    private Long id;
    private String especie;
    private String ubicacion;
    private LocalDate fecha;
    private String estado;

    // Constructor vacío
    public Arbol() {
    }

    // Constructor con parámetros
    public Arbol(Long id, String especie, String ubicacion, LocalDate fecha, String estado) {
        this.id = id;
        this.especie = especie;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
