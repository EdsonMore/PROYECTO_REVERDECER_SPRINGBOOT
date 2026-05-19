package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por correo electrónico
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Busca un usuario por nombre de usuario (user)
     */
    Optional<Usuario> findByUser(String user);

    /**
     * Busca un usuario por DNI
     */
    Optional<Usuario> findByDni(String dni);

    /**
     * Obtiene todos los usuarios activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombres ASC")
    List<Usuario> findAllActivos();

    /**
     * Verifica si existe un usuario con ese correo
     */
    boolean existsByCorreo(String correo);

    /**
     * Verifica si existe un usuario con ese DNI
     */
    boolean existsByDni(String dni);

    /**
     * Verifica si existe un usuario con ese user
     */
    boolean existsByUser(String user);

    /**
     * Busca usuarios por nombre (parcial)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) LIKE LOWER(CONCAT('%', :nombre, '%')) AND u.activo = true")
    List<Usuario> findByNombresContaining(@Param("nombre") String nombre);
}
