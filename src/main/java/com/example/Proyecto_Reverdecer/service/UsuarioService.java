package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * CAPA DE SERVICIO - UsuarioService
 * ========================================
 * 
 * Responsabilidad: Lógica de negocio de autenticación
 * 
 * Procesa:
 * 1. REGISTRO: Validar datos, verificar email único, guardar usuario
 * 2. LOGIN: Validar credenciales contra la "base de datos"
 * 3. ACTUALIZACIÓN: Modificar datos del usuario
 * 
 * Esta capa es la "inteligencia" del sistema:
 * - Valida reglas de negocio
 * - Manipula datos antes de guardar/recuperar
 * - Centraliza la lógica reutilizable
 * 
 * Flujo de datos:
 * Controller (recibe HTTP) 
 *   → Service (procesa lógica)
 *     → Repository/BD (persiste datos)
 *       → Service (retorna resultado)
 *         → Controller (genera respuesta HTTP)
 *           → HTML/Thymeleaf (renderiza)
 */
@Service
public class UsuarioService {
    
    // Simulamos una "base de datos" en memoria
    // En producción: esto sería inyectado un UsuarioRepository que accede a DB
    private static final Map<String, Usuario> BD_USUARIOS = new HashMap<>();
    
    // Contador simple para IDs
    private static long contador = 1;
    
    /**
     * MÉTODO: REGISTRAR USUARIO
     * 
     * Lógica:
     * 1. Validar que email no esté vacío
     * 2. Validar que password no esté vacío
     * 3. Validar que email no esté duplicado
     * 4. Guardar el usuario con ID único
     * 
     * @param usuario Objeto Usuario con datos del formulario
     * @return true si registro fue exitoso, false si falló
     * @throws IOException si hay error de I/O
     */
    public boolean registrar(Usuario usuario) throws java.io.IOException {
        
        // VALIDACIÓN 1: Email no vacío
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            System.out.println("[ERROR] Email vacío");
            return false;
        }
        
        // VALIDACIÓN 2: Password no vacío
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            System.out.println("[ERROR] Password vacío");
            return false;
        }
        
        // VALIDACIÓN 3: Email no duplicado
        // Estructura de control IF: Verificar si email ya existe
        if (BD_USUARIOS.containsKey(usuario.getCorreo())) {
            System.out.println("[ERROR] Email ya registrado: " + usuario.getCorreo());
            return false;
        }
        
        // ASIGNACIÓN DE ID
        usuario.setId(contador++);
        
        // GUARDAR en la "base de datos"
        BD_USUARIOS.put(usuario.getCorreo(), usuario);
        
        System.out.println("[ÉXITO] Usuario registrado: " + usuario.getCorreo() + " con ID: " + usuario.getId());
        return true;
    }
    
    /**
     * MÉTODO: AUTENTICAR (LOGIN)
     * 
     * Lógica:
     * 1. Validar que email y password no sean vacíos
     * 2. Buscar usuario por email
     * 3. Verificar que password coincida
     * 4. Retornar usuario si coincide, null si no
     * 
     * @param correo Email del usuario
     * @param password Password a validar
     * @return Usuario si credenciales son correctas, null si no
     */
    public Usuario autenticar(String correo, String password) {
        
        // VALIDACIÓN: Email no vacío
        if (correo == null || correo.trim().isEmpty()) {
            System.out.println("[ERROR LOGIN] Email vacío");
            return null;
        }
        
        // VALIDACIÓN: Password no vacío
        if (password == null || password.trim().isEmpty()) {
            System.out.println("[ERROR LOGIN] Password vacío");
            return null;
        }
        
        // BÚSQUEDA: Estructura de control - buscar usuario
        // Usamos Optional para manejar nulls de forma segura
        Optional<Usuario> usuarioOpt = Optional.ofNullable(BD_USUARIOS.get(correo));
        
        // Si usuario existe, validar password
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // COMPARACIÓN de password
            // En producción: usar BCrypt para comparar contraseñas hasheadas
            if (usuario.getPassword().equals(password)) {
                System.out.println("[ÉXITO LOGIN] Usuario autenticado: " + correo);
                return usuario;
            } else {
                System.out.println("[ERROR LOGIN] Password incorrecto para: " + correo);
            }
        } else {
            System.out.println("[ERROR LOGIN] Usuario no encontrado: " + correo);
        }
        
        return null; // Login fallido
    }
    
    /**
     * MÉTODO: ACTUALIZAR PERFIL
     * 
     * Lógica:
     * 1. Buscar usuario por ID
     * 2. Actualizar sus datos
     * 3. Guardar cambios
     * 4. Retornar usuario actualizado
     * 
     * @param usuarioActualizado Usuario con datos nuevos
     * @return Usuario después de actualizar
     * @throws IOException si hay error
     */
    public Usuario actualizar(Usuario usuarioActualizado) throws java.io.IOException {
        
        // Buscar usuario original por email
        if (usuarioActualizado.getCorreo() != null && 
            BD_USUARIOS.containsKey(usuarioActualizado.getCorreo())) {
            
            Usuario usuarioOriginal = BD_USUARIOS.get(usuarioActualizado.getCorreo());
            
            // Actualizar campos (si no son vacíos)
            if (usuarioActualizado.getNombres() != null && !usuarioActualizado.getNombres().isEmpty()) {
                usuarioOriginal.setNombres(usuarioActualizado.getNombres());
            }
            if (usuarioActualizado.getApellidoPaterno() != null && !usuarioActualizado.getApellidoPaterno().isEmpty()) {
                usuarioOriginal.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
            }
            if (usuarioActualizado.getGenero() != null && !usuarioActualizado.getGenero().isEmpty()) {
                usuarioOriginal.setGenero(usuarioActualizado.getGenero());
            }
            
            System.out.println("[ACTUALIZACIÓN] Usuario actualizado: " + usuarioOriginal.getCorreo());
            return usuarioOriginal;
        }
        
        return null;
    }
    
    /**
     * MÉTODO: VERIFICAR SI EMAIL EXISTE
     * 
     * Útil para validación en tiempo real desde frontend
     * 
     * @param email Email a verificar
     * @return true si existe, false si no
     */
    public boolean emailExiste(String email) {
        return BD_USUARIOS.containsKey(email);
    }
    
    /**
     * MÉTODO: OBTENER USUARIO POR EMAIL (interno)
     * 
     * @param email Email del usuario
     * @return Usuario si existe, null si no
     */
    public Usuario obtenerPorEmail(String email) {
        return BD_USUARIOS.get(email);
    }
}
