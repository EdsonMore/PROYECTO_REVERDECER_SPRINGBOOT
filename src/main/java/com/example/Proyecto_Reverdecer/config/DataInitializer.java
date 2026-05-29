package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.model.TipoDoc;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;


//Aca estams creando usuarios de prueba por defecto, como admin y gestor
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        crearAdminPorDefecto();
        crearGestorPorDefecto();
    }

    private void crearAdminPorDefecto() {

        // Verificar si ya existe un usuario admin
        boolean adminExists = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getIsAdmin() != null && u.getIsAdmin());

        if (adminExists) {
            System.out.println("Admin ya existe en la base de datos");
            return;
        }

        System.out.println("No hay admin en la BD. Creando admin por defecto...");

        Usuario admin = crearUsuario(
                "admin",
                "admin123",
                "admin@reverdecerpiura.com",
                "Administrador",
                "Sistema",
                "ReVerdecer",
                "00000000");
        admin.setIsAdmin(true);

        guardarUsuario(admin, "Admin");
    }

    private void crearGestorPorDefecto() {

        // Verificar si ya existe un gestor
        boolean gestorExists = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getRol() != null && u.getRol().equals("ROLE_GESTOR_AMBIENTAL"));

        if (gestorExists) {
            System.out.println("Gestor Ambiental ya existe en la base de datos");
            return;
        }

        System.out.println("No hay Gestor Ambiental. Creando gestor por defecto...");

        Usuario gestor = crearUsuario(
                "gestor",
                "gestor123",
                "gestor@reverdecerpiura.com",
                "Gestor",
                "Ambiental",
                "ReVerdecer",
                "11111111");
        gestor.setRol("ROLE_GESTOR_AMBIENTAL");

        guardarUsuario(gestor, "Gestor Ambiental");
    }

    
     // Crea un usuario con datos básicos
     
    private Usuario crearUsuario(String user, String password, String correo,
            String nombres, String apellidoPaterno,
            String apellidoMaterno, String dni) {
        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setCorreo(correo);
        usuario.setNombres(nombres);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setDni(dni);
        usuario.setTipoDoc(TipoDoc.DNI);
        usuario.setGenero("Otro");
        usuario.setDireccion1("Sistema");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDate.now());

        return usuario;
    }

    
     // Guarda un usuario y muestra mensajes
     
    private void guardarUsuario(Usuario usuario, String tipo) {
        try {
            usuarioRepository.save(usuario);
            System.out.println("creado exitosamente!");
            System.out.println("Usuario: " + usuario.getUser());
            System.out.println("Correo: " + usuario.getCorreo());
            if (usuario.getUser().equals("admin")) {
                System.out.println("Contraseña: admin123");
            } else if (usuario.getUser().equals("gestor")) {
                System.out.println("Contraseña: gestor123");
            } else if (usuario.getUser().equals("usuario")) {
                System.out.println("Contraseña: usuario123");
            }
        } catch (Exception e) {
            System.err.println("Error al crear " + tipo + ": " + e.getMessage());
        }
    }
}
