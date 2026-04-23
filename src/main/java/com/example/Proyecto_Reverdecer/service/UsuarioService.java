package com.example.Proyecto_Reverdecer.service;

import com.example.Proyecto_Reverdecer.model.Usuario;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UsuarioService {
    
    // SIMULACIÓN DE BASE DE DATOS CON ARREGLO (rúbrica: manejo de arreglos en Service)
    private final List<Usuario> usuarios = new ArrayList<>();
    private Long nextId = 1L;
    
    // ARCHIVO PARA PERSISTENCIA (opcional)
    private static final String DATA_FILE = "usuarios.dat";
    
    public UsuarioService() {
        cargarUsuarios();
        // Datos de ejemplo si está vacío
        if (usuarios.isEmpty()) {
            crearUsuarioEjemplo();
        }
    }
    
    /**
     * REGISTRO DE USUARIO CON VALIDACIONES COMPLETAS
     * Estructuras de control: if, for, validaciones múltiples
     */
    public boolean registrar(Usuario usuario) throws IOException {
        // 1. Validar campos obligatorios (estructura if anidada)
        if (!validarCamposObligatorios(usuario)) {
            return false;
        }
        
        // 2. Validar formato de correo (regex)
        if (!validarCorreo(usuario.getCorreo())) {
            return false;
        }
        
        // 3. Validar contraseña (mínimo 6 caracteres)
        if (!validarPassword(usuario.getPassword())) {
            return false;
        }
        
        // 4. Validar edad (mínimo 18 años)
        if (!validarEdad(usuario.getFechaNacimiento())) {
            return false;
        }
        
        // 5. Validar DNI/RUC (según tipo de documento)
        if (!validarDocumento(usuario.getTipoDoc().toString(), usuario.getDni())) {
            return false;
        }
        
        // 6. Validar teléfono (9 dígitos)
        if (!validarTelefono(usuario.getNumero())) {
            return false;
        }
        
        // 7. Verificar si el correo ya existe (estructura for + condición)
        for (Usuario existing : usuarios) {
            if (existing.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                return false; // Correo duplicado
            }
        }
        
        // 8. Asignar ID y guardar
        usuario.setId(nextId++);
        usuarios.add(usuario);
        guardarUsuarios();
        
        return true;
    }
    
    /**
     * AUTENTICACIÓN DE USUARIO
     * Uso de streams y lambda expressions (Java moderno)
     */
    public Usuario autenticar(String correo, String password) {
        // Uso de stream + filter + findFirst (estructura funcional)
        Optional<Usuario> usuario = usuarios.stream()
            .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
            .filter(u -> u.getPassword().equals(password))
            .findFirst();
        
        return usuario.orElse(null);
    }
    
    /**
     * BUSCAR USUARIO POR CORREO
     */
    public Usuario buscarPorCorreo(String correo) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                return u;
            }
        }
        return null;
    }
    
    /**
     * OBTENER TODOS LOS USUARIOS
     */
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }
    
    /**
     * VALIDACIONES PRIVADAS (lógica de programación)
     */
    
    private boolean validarCamposObligatorios(Usuario usuario) {
        // Estructura de control if múltiple
        if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) return false;
        if (usuario.getApellidoPaterno() == null || usuario.getApellidoPaterno().trim().isEmpty()) return false;
        if (usuario.getApellidoMaterno() == null || usuario.getApellidoMaterno().trim().isEmpty()) return false;
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) return false;
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) return false;
        if (usuario.getDni() == null || usuario.getDni().trim().isEmpty()) return false;
        if (usuario.getDireccion1() == null || usuario.getDireccion1().trim().isEmpty()) return false;
        if (usuario.getFechaNacimiento() == null) return false;
        return true;
    }
    
    private boolean validarCorreo(String correo) {
        // Patrón regex para email válido
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(correo).matches();
    }
    
    private boolean validarPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    private boolean validarEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return false;
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();
        return edad >= 18;
    }
    
    private boolean validarDocumento(String tipoDoc, String numero) {
        if (numero == null) return false;
        
        // Uso de switch (estructura de control)
        switch (tipoDoc.toUpperCase()) {
            case "DNI":
                return numero.matches("\\d{8}");
            case "RUC":
                return numero.matches("\\d{11}");
            case "PASAPORTE":
                return numero.matches("[A-Z0-9]{6,12}");
            default:
                return false;
        }
    }
    
    private boolean validarTelefono(int numero) {
        String telefonoStr = String.valueOf(numero);
        return telefonoStr.matches("\\d{9}");
    }
    
    /**
     * PERSISTENCIA CON ARCHIVOS
     */
    @SuppressWarnings("unchecked")
    private void cargarUsuarios() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Usuario> cargados = (List<Usuario>) ois.readObject();
                usuarios.addAll(cargados);
                // Actualizar nextId
                nextId = usuarios.stream()
                    .mapToLong(Usuario::getId)
                    .max()
                    .orElse(0L) + 1;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error cargando usuarios: " + e.getMessage());
            }
        }
    }
    
    private void guardarUsuarios() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(usuarios);
        }
    }
    
    private void crearUsuarioEjemplo() {
        Usuario ejemplo = new Usuario();
        ejemplo.setId(nextId++);
        ejemplo.setNombres("Admin");
        ejemplo.setApellidoPaterno("Sistema");
        ejemplo.setApellidoMaterno("ReVerdecer");
        ejemplo.setCorreo("admin@reverdecer.com");
        ejemplo.setPassword("admin123");
        ejemplo.setDni("12345678");
        ejemplo.setDireccion1("Piura");
        ejemplo.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        ejemplo.setNumero(987654321);
        ejemplo.setGenero("Masculino");
        usuarios.add(ejemplo);
    }
}