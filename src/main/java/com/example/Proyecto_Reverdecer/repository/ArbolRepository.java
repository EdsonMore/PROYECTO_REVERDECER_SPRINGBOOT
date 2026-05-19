package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Arbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Arbol
 */
@Repository
public interface ArbolRepository extends JpaRepository<Arbol, Long> {

    /**
     * Obtiene todos los árboles de un usuario específico
     */
    List<Arbol> findByUsuarioId(Long usuarioId);

    /**
     * Obtiene todos los árboles de un usuario ordenados por fecha
     */
    @Query("SELECT a FROM Arbol a WHERE a.usuario.id = :usuarioId ORDER BY a.fechaRegistro DESC")
    List<Arbol> findByUsuarioIdOrderByFecha(@Param("usuarioId") Long usuarioId);

    /**
     * Busca árboles por especie
     */
    List<Arbol> findByEspecie(String especie);

    /**
     * Busca árboles por estado
     */
    List<Arbol> findByEstado(String estado);

    /**
     * Busca árboles por ubicación (parcial)
     */
    @Query("SELECT a FROM Arbol a WHERE LOWER(a.ubicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%'))")
    List<Arbol> findByUbicacionContaining(@Param("ubicacion") String ubicacion);

    /**
     * Obtiene los árboles de un usuario con un estado específico
     */
    @Query("SELECT a FROM Arbol a WHERE a.usuario.id = :usuarioId AND a.estado = :estado")
    List<Arbol> findByUsuarioIdAndEstado(@Param("usuarioId") Long usuarioId, @Param("estado") String estado);

    /**
     * Cuenta los árboles registrados por un usuario
     */
    long countByUsuarioId(Long usuarioId);

    /**
     * Cuenta los árboles por estado
     */
    long countByEstado(String estado);

    /**
     * Obtiene todos los árboles activos
     */
    @Query("SELECT a FROM Arbol a WHERE a.estado != 'MUERTO' ORDER BY a.fechaRegistro DESC")
    List<Arbol> findAllActivos();
}
