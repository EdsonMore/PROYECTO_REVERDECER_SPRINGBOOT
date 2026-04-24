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
    private final List<Usuario> usuarios = new ArrayList<>();
    private Long nextId = 1L;
    
    // Mejor ubicación para el archivo (dentro del directorio del usuario)
    private static final String DATA_DIR = System.getProperty("user.home") + "/.reverdecer/";
    private static final String DATA_FILE = DATA_DIR + "usuarios.dat";
    
    public UsuarioService() {
        // Crear directorio si no existe
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        cargarUsuarios();
        if (usuarios.isEmpty()) {
            crearUsuarioEjemplo();
            try {
                guardarUsuarios(); // Guardar el usuario ejemplo
            } catch (IOException e) {
                System.err.println("Error guardando usuario ejemplo: " + e.getMessage());
            }
        }
    }
 
    public boolean registrar(Usuario usuario) throws IOException {
        System.out.println("\n=== SERVICE: REGISTRO ===");
        
        // Validar campos mínimos
        if (!validarCamposMinimos(usuario)) {
            return false;
        }
        
        // Validar formato de correo
        if (!validarCorreo(usuario.getCorreo())) {
            return false;
        }
        
        // Validar contraseña
        if (!validarPassword(usuario.getPassword())) {
            return false;
        }
        
        // Validar edad (solo si se proporcionó)
        if (usuario.getFechaNacimiento() != null && !validarEdad(usuario.getFechaNacimiento())) {
            return false;
        }
        
        // Validar documento (solo si se proporcionó)
        if (usuario.getTipoDoc() != null && usuario.getDni() != null && !usuario.getDni().isEmpty()) {
            if (!validarDocumento(usuario.getTipoDoc().toString(), usuario.getDni())) {
                return false;
            }
        }
        
        // Validar teléfono (solo si se proporcionó)
        if (usuario.getNumero() > 0 && !validarTelefono(usuario.getNumero())) {
            return false;
        }
        
        // Verificar correo duplicado
        for (Usuario existing : usuarios) {
            if (existing.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                System.out.println("Correo ya registrado: " + usuario.getCorreo());
                return false;
            }
        }
        
        // Valores por defecto
        if (usuario.getGenero() == null || usuario.getGenero().isEmpty()) {
            usuario.setGenero("No especificado");
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
        
        // Asignar ID y guardar
        usuario.setId(nextId++);
        usuarios.add(usuario);
        
        // Guardar en archivo
        guardarUsuarios();
        
        System.out.println(" REGISTRO EXITOSO! ID: " + usuario.getId());
        System.out.println("  - Correo: " + usuario.getCorreo());
        System.out.println("  - Nombre: " + usuario.getNombres());
        System.out.println("  - Archivo: " + DATA_FILE);
        
        return true;
    }
    
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
            System.out.println(" Contraseña es obligatoria");
            return false;
        }
        return true;
    }
  
    public Usuario autenticar(String correo, String password) {
        System.out.println("\n=== SERVICE: AUTENTICACIÓN ===");
        System.out.println("Buscando: " + correo);
        
        Optional<Usuario> usuario = usuarios.stream()
            .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
            .filter(u -> u.getPassword().equals(password))
            .findFirst();
        
        if (usuario.isPresent()) {
            System.out.println(" Usuario encontrado: " + usuario.get().getNombres());
        } else {
            System.out.println("Usuario NO encontrado");
            System.out.println("Usuarios registrados: " + usuarios.size());
            for (Usuario u : usuarios) {
                System.out.println("  - " + u.getCorreo());
            }
        }
        
        return usuario.orElse(null);
    }
    
    public Usuario buscarPorCorreo(String correo) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                return u;
            }
        }
        return null;
    }
    
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }
    
    
    private boolean validarCorreo(String correo) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(correo).matches();
    }
    
    private boolean validarPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    private boolean validarEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return true;
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();
        return edad >= 18;
    }
    
    private boolean validarDocumento(String tipoDoc, String numero) {
        if (numero == null) return false;
        
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
    
    //PERSISTENCIA
    
    @SuppressWarnings("unchecked")
    private void cargarUsuarios() {
        File file = new File(DATA_FILE);
        System.out.println("Cargando usuarios desde: " + file.getAbsolutePath());
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Usuario> cargados = (List<Usuario>) ois.readObject();
                usuarios.addAll(cargados);
                
                // Actualizar nextId
                nextId = usuarios.stream()
                    .mapToLong(Usuario::getId)
                    .max()
                    .orElse(0L) + 1;
                    
                System.out.println("Cargados " + usuarios.size() + " usuarios desde archivo");
                
            } catch (FileNotFoundException e) {
                System.out.println("Archivo no encontrado, se creará uno nuevo");
            } catch (IOException e) {
                System.err.println("Error de IO al cargar: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("Error de clase al cargar: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Archivo no existe, se creará al primer registro");
        }
    }
    
    private void guardarUsuarios() throws IOException {
        File file = new File(DATA_FILE);
        
        // Asegurar que el directorio existe
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(usuarios);
            System.out.println("Guardados " + usuarios.size() + " usuarios en: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
            throw e;
        }
    }
    
    private void crearUsuarioEjemplo() {
        Usuario ejemplo = new Usuario();
        ejemplo.setId(nextId++);
        ejemplo.setNombres("Jean");
        ejemplo.setApellidoPaterno("Paiva");
        ejemplo.setApellidoMaterno("More");
        ejemplo.setCorreo("user@gmail.com");
        ejemplo.setPassword("User123");
        ejemplo.setDni("23456786");
        ejemplo.setDireccion1("Piura");
        ejemplo.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        ejemplo.setNumero(987654321);
        ejemplo.setGenero("Masculino");
        usuarios.add(ejemplo);
      
    }
}