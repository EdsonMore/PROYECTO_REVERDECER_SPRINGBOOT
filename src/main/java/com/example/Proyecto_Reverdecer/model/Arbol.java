package com.example.Proyecto_Reverdecer.model;

import java.time.LocalDate;
import java.io.Serializable;

import jakarta.persistence.*;

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

    public Arbol() {
    }

    public Arbol(Long id, String especie, String ubicacion, LocalDate fechaPlantacion, String estado) {
        this.id = id;
        this.especie = especie;
        this.ubicacion = ubicacion;
        this.fechaPlantacion = fechaPlantacion;
        this.estado = estado;
    }

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

    public LocalDate getFechaPlantacion() {
        return fechaPlantacion;
    }

    public void setFechaPlantacion(LocalDate fechaPlantacion) {
        this.fechaPlantacion = fechaPlantacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}