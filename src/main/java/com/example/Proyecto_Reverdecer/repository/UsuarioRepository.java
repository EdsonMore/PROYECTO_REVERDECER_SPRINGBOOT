package com.example.Proyecto_Reverdecer.repository;

import com.example.Proyecto_Reverdecer.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscamos por correo
    Optional<Usuario> findByCorreo(String correo);

    // Buscamos por nombre de usuario
    Optional<Usuario> findByUser(String user);

    // Buscamos por DNI
    Optional<Usuario> findByDni(String dni);

    // Todos los usuarios activos ordenados por nombre
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombres ASC")
    List<Usuario> findAllActivos();

    // Verificamos si ya existe ese correo
    boolean existsByCorreo(String correo);

    // Verificamos si ya existe ese DNI
    boolean existsByDni(String dni);

    // Verificamos si ya existe ese nombre de usuario
    boolean existsByUser(String user);

    // Buscamos por nombre sin importar mayúsculas
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) LIKE LOWER(CONCAT('%', :nombre, '%')) AND u.activo = true")
    List<Usuario> findByNombresContaining(@Param("nombre") String nombre);
}