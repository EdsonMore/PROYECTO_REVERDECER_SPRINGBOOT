// src/main/java/com/example/Proyecto_Reverdecer/model/TipoDoc.java
package com.example.Proyecto_Reverdecer.model;

/**
 * Enum para los tipos de documentos aceptados
 */
public enum TipoDoc {
    DNI("Documento Nacional de Identidad"),
    RUC("Registro Único de Contribuyente"),
    PASAPORTE("Pasaporte"),
    CARNET_EXTRANJERIA("Carné de Extranjería");
    
    private final String descripcion;
    
    TipoDoc(String descripcion) {
        this.descripcion = descripcion;
    }
    

    public String getDescripcion() {
        return descripcion;
    }
}

