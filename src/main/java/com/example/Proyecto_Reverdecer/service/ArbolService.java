package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Arbol;
import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.repository.ArbolRepository;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar árboles con JPA
 */
@Service
public class ArbolService {

    @Autowired
    private ArbolRepository arbolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra un nuevo árbol
     */
    public boolean registrar(Arbol arbol, Long usuarioId) {
        System.out.println("\n=== SERVICE: REGISTRO ÁRBOL ===");

        if (!validarCamposMinimos(arbol)) {
            return false;
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            System.out.println("Usuario no encontrado");
            return false;
        }

        arbol.setUsuario(usuarioOpt.get());
        arbol.setFechaRegistro(LocalDate.now());
        if (arbol.getFechaPlantacion() == null) {
            arbol.setFechaPlantacion(LocalDate.now());
        }

        try {
            Arbol savedArbol = arbolRepository.save(arbol);
            System.out.println("✓ ÁRBOL REGISTRADO! ID: " + savedArbol.getId());
            System.out.println("  - Especie: " + savedArbol.getEspecie());
            System.out.println("  - Ubicación: " + savedArbol.getUbicacion());
            System.out.println("  - Usuario: " + savedArbol.getUsuario().getNombres());
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar árbol: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los árboles de un usuario
     */
    public List<Arbol> obtenerPorUsuario(Long usuarioId) {
        return arbolRepository.findByUsuarioIdOrderByFecha(usuarioId);
    }

    /**
     * Obtiene un árbol por ID
     */
    public Arbol obtenerPorId(Long id) {
        return arbolRepository.findById(id).orElse(null);
    }

    /**
     * Lista todos los árboles (método compatible con código anterior)
     */
    public List<Arbol> listarTodos() {
        return arbolRepository.findAll();
    }

    /**
     * Lista árboles por estado
     */
    public List<Arbol> listarPorEstado(String estado) {
        return arbolRepository.findByEstado(estado);
    }

    /**
     * Guarda un árbol (método compatible con código anterior)
     */
    public Arbol guardar(Arbol arbol) {
        System.out.println("\n=== SERVICE: GUARDAR ÁRBOL ===");
        System.out.println("Nombre: " + arbol.getNombre());
        System.out.println("Especie: " + arbol.getEspecie());
        System.out.println("Usuario: " + (arbol.getUsuario() != null ? arbol.getUsuario().getNombres() : "NULL"));
        System.out.println("Usuario ID: " + (arbol.getUsuario() != null ? arbol.getUsuario().getId() : "NULL"));
        
        if (arbol.getUsuario() == null) {
            System.err.println("❌ ERROR: El árbol no tiene usuario asignado");
            throw new RuntimeException("No se puede guardar un árbol sin usuario");
        }
        
        if (arbol.getUsuario().getId() == null) {
            System.err.println("❌ ERROR: El usuario del árbol no tiene ID válido");
            throw new RuntimeException("El usuario del árbol no tiene ID válido");
        }

        if (arbol.getId() == null) {
            arbol.setFechaPlantacion(LocalDate.now());
            arbol.setFechaRegistro(LocalDate.now());
        }
        
        try {
            Arbol guardado = arbolRepository.save(arbol);
            System.out.println("✅ Árbol guardado con ID: " + guardado.getId());
            return guardado;
        } catch (Exception e) {
            System.err.println("❌ Error al guardar árbol: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Actualiza un árbol existente
     */
    public boolean actualizar(Arbol arbol) {
        if (arbol.getId() == null) {
            return false;
        }

        try {
            if (!arbolRepository.existsById(arbol.getId())) {
                return false;
            }
            arbolRepository.save(arbol);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar árbol: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un árbol
     */
    public boolean eliminar(Long id) {
        try {
            if (arbolRepository.existsById(id)) {
                arbolRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al eliminar árbol: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene árboles por especie
     */
    public List<Arbol> obtenerPorEspecie(String especie) {
        return arbolRepository.findByEspecie(especie);
    }

    /**
     * Obtiene todos los árboles activos
     */
    public List<Arbol> obtenerActivos() {
        return arbolRepository.findAllActivos();
    }

    /**
     * Cuenta los árboles de un usuario
     */
    public long contar(Long usuarioId) {
        return arbolRepository.countByUsuarioId(usuarioId);
    }

    /**
     * Valida campos mínimos del árbol
     */
    private boolean validarCamposMinimos(Arbol arbol) {
        if (arbol.getEspecie() == null || arbol.getEspecie().trim().isEmpty()) {
            System.out.println("Especie es obligatoria");
            return false;
        }
        if (arbol.getUbicacion() == null || arbol.getUbicacion().trim().isEmpty()) {
            System.out.println("Ubicación es obligatoria");
            return false;
        }
        if (arbol.getEstado() == null || arbol.getEstado().trim().isEmpty()) {
            System.out.println("Estado es obligatorio");
            return false;
        }
        return true;
    }
}