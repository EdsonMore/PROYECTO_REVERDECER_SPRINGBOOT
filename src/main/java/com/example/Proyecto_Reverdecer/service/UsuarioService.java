package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en la base de datos
     */
    public boolean registrar(Usuario usuario) {
        System.out.println("\n=== SERVICE: REGISTRO ===");

        if (!validarCamposMinimos(usuario)) {
            return false;
        }

        if (!validarCorreo(usuario.getCorreo())) {
            System.out.println("Correo inválido: " + usuario.getCorreo());
            return false;
        }

        if (!validarPassword(usuario.getPassword())) {
            System.out.println("Contraseña debe tener mínimo 6 caracteres");
            return false;
        }

        if (usuario.getFechaNacimiento() != null && !validarEdad(usuario.getFechaNacimiento())) {
            System.out.println("Debe ser mayor de 18 años");
            return false;
        }

        if (usuario.getTipoDoc() != null && usuario.getDni() != null && !usuario.getDni().isEmpty()) {
            if (!validarDocumento(usuario.getTipoDoc().toString(), usuario.getDni())) {
                System.out.println("Documento inválido");
                return false;
            }
        }

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            System.out.println("Correo ya registrado: " + usuario.getCorreo());
            return false;
        }

        if (usuarioRepository.existsByDni(usuario.getDni())) {
            System.out.println("DNI ya registrado: " + usuario.getDni());
            return false;
        }

        // Generar usuario automáticamente si no viene
        if (usuario.getUser() == null || usuario.getUser().isEmpty()) {
            String userGenerado = generarUsuario(usuario.getNombres(), usuario.getCorreo());
            usuario.setUser(userGenerado);
        }

        // Verificar que el usuario sea único
        if (usuarioRepository.existsByUser(usuario.getUser())) {
            System.out.println("Usuario ya existe: " + usuario.getUser());
            return false;
        }

        // Establecer valores por defecto
        if (usuario.getGenero() == null || usuario.getGenero().isEmpty()) {
            usuario.setGenero("Otro");
        }
        if (usuario.getDireccion1() == null || usuario.getDireccion1().isEmpty()) {
            usuario.setDireccion1("No especificada");
        }
        if (usuario.getApellidoPaterno() == null) {
            usuario.setApellidoPaterno("");
        }
        if (usuario.getApellidoMaterno() == null) {
            usuario.setApellidoMaterno("");
        }

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDate.now());
        
        // Asignar rol por defecto si no tiene
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ROLE_USER");
        }
        
        // Asignar isAdmin por defecto si no tiene
        if (usuario.getIsAdmin() == null) {
            usuario.setIsAdmin(false);
        }

        try {
            Usuario savedUsuario = usuarioRepository.save(usuario);
            System.out.println(" REGISTRO EXITOSO! ID: " + savedUsuario.getId());
            System.out.println("  - Correo: " + savedUsuario.getCorreo());
            System.out.println("  - Nombre: " + savedUsuario.getNombres());
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Autentica un usuario con correo y contraseña
     */
    public Usuario autenticar(String correo, String password) {
        System.out.println("\n=== SERVICE: AUTENTICACIÓN ===");
        System.out.println("Buscando: " + correo);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("✓ Usuario encontrado en BD");
            System.out.println("  - ID: " + usuario.getId());
            System.out.println("  - Correo: " + usuario.getCorreo());
            System.out.println("  - Activo: " + usuario.getActivo());

            if (!usuario.getActivo()) {
                System.out.println("❌ Usuario inactivo");
                return null;
            }
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                System.out.println("✓ Contraseña correcta");
                System.out.println("✓ Retornando usuario con ID: " + usuario.getId());
                return usuario;
            }
            System.out.println("❌ Contraseña incorrecta");
        } else {
            System.out.println("❌ Usuario NO encontrado en BD");
        }

        System.out.println("Usuario NO encontrado o contraseña incorrecta");
        return null;
    }

    /**
     * Busca un usuario por correo
     */
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    /**
     * Busca un usuario por ID
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene todos los usuarios activos
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAllActivos();
    }

    /**
     * Obtiene todos los usuarios (incluyendo inactivos)
     */
    public List<Usuario> listarTodosAdmin() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca usuarios por nombre
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombresContaining(nombre);
    }

    /**
     * Actualiza un usuario existente
     */
    public boolean actualizar(Usuario usuario) {
        if (usuario.getId() == null) {
            return false;
        }
        
        try {
            Usuario existente = usuarioRepository.findById(usuario.getId()).orElse(null);
            if (existente == null) {
                return false;
            }
            
            // No actualizar la contraseña aquí, usar método separado
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(existente.getPassword());
            } else {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            
            usuarioRepository.save(usuario);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cambia la contraseña de un usuario
     */
    public boolean cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            System.out.println("Contraseña actual incorrecta");
            return false;
        }
        
        if (!validarPassword(passwordNueva)) {
            return false;
        }
        
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
        return true;
    }

    /**
     * Desactiva un usuario
     */
    public boolean desactivar(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        return true;
    }

    /**
     * Valida campos mínimos del usuario
     */
    private boolean validarCamposMinimos(Usuario usuario) {
        if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) {
            System.out.println("Nombres es obligatorio");
            return false;
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            System.out.println("Correo es obligatorio");
            return false;
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            System.out.println("Contraseña es obligatoria");
            return false;
        }
        if (usuario.getDni() == null || usuario.getDni().trim().isEmpty()) {
            System.out.println("DNI es obligatorio");
            return false;
        }
        return true;
    }

    /**
     * Valida el formato del correo electrónico
     */
    private boolean validarCorreo(String correo) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(correo).matches();
    }

    /**
     * Valida la contraseña (mínimo 6 caracteres)
     */
    private boolean validarPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Valida que el usuario sea mayor de 18 años
     */
    private boolean validarEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null)
            return true;
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();
        return edad >= 18;
    }

    /**
     * Valida el formato del documento según el tipo
     */
    private boolean validarDocumento(String tipoDoc, String numero) {
        if (numero == null)
            return false;

        switch (tipoDoc.toUpperCase()) {
            case "DNI":
                return numero.matches("\\d{8}");
            case "RUC":
                return numero.matches("\\d{11}");
            case "PASAPORTE":
                return numero.matches("[A-Z0-9]{6,12}");
            case "CARNET_EXTRANJERIA":
                return numero.matches("[A-Z0-9]{6,12}");
            default:
                return false;
        }
    }

    /**
     * Genera un nombre de usuario único a partir del correo o nombres
     */
    private String generarUsuario(String nombres, String correo) {
        String baseUsuario;
        
        // Prioridad: usar la parte del email antes del @
        if (correo != null && correo.contains("@")) {
            baseUsuario = correo.substring(0, correo.indexOf("@")).toLowerCase();
        } else if (nombres != null && !nombres.isEmpty()) {
            // Si no hay email válido, usar el primer nombre
            baseUsuario = nombres.split("\\s+")[0].toLowerCase();
        } else {
            // Fallback: generar uno aleatorio
            baseUsuario = "usuario" + System.currentTimeMillis() % 10000;
        }
        
        // Remover caracteres especiales y espacios
        baseUsuario = baseUsuario.replaceAll("[^a-z0-9]", "");
        
        // Asegurar que sea único agregando un número si es necesario
        String usuarioFinal = baseUsuario;
        int contador = 1;
        while (usuarioRepository.existsByUser(usuarioFinal)) {
            usuarioFinal = baseUsuario + contador;
            contador++;
        }
        
        return usuarioFinal;
    }
}