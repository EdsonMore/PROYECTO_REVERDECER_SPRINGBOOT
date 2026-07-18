// src/main/java/com/example/Proyecto_Reverdecer/repository/ArbolRepository.java
package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Arbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArbolRepository extends JpaRepository<Arbol, Long> {

    // Árboles de un usuario
    List<Arbol> findByUsuarioId(Long usuarioId);

    // Árboles de un usuario ordenados por fecha (más reciente primero)
    @Query("SELECT a FROM Arbol a WHERE a.usuario.id = :usuarioId ORDER BY a.fechaRegistro DESC")
    List<Arbol> findByUsuarioIdOrderByFecha(@Param("usuarioId") Long usuarioId);

    // Por especie exacta
    List<Arbol> findByEspecie(String especie);

    List<Arbol> findByEstado(String estado);

    // Por ubicación (búsqueda parcial sin importar mayúsculas)
    @Query("SELECT a FROM Arbol a WHERE LOWER(a.ubicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%'))")
    List<Arbol> findByUbicacionContaining(@Param("ubicacion") String ubicacion);

    // Árboles de un usuario con estado específico
    @Query("SELECT a FROM Arbol a WHERE a.usuario.id = :usuarioId AND a.estado = :estado")
    List<Arbol> findByUsuarioIdAndEstado(@Param("usuarioId") Long usuarioId, @Param("estado") String estado);

    // Cuántos árboles tiene un usuario
    long countByUsuarioId(Long usuarioId);

    // Cuántos árboles hay en un estado
    long countByEstado(String estado);

    // Todos los árboles excepto los muertos
    @Query("SELECT a FROM Arbol a WHERE a.estado != 'MUERTO' ORDER BY a.fechaRegistro DESC")
    List<Arbol> findAllActivos();
}