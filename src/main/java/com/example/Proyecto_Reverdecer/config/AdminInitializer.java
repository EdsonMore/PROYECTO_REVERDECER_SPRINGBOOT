package com.example.Proyecto_Reverdecer.config;

import com.example.Proyecto_Reverdecer.model.Usuario;
import com.example.Proyecto_Reverdecer.model.TipoDoc;
import com.example.Proyecto_Reverdecer.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== INICIALIZADOR DE ADMIN ===");
        
        // Verificar si ya existe un usuario admin
        boolean adminExists = usuarioRepository.findAll().stream()
            .anyMatch(u -> u.getIsAdmin() != null && u.getIsAdmin());
        
        if (adminExists) {
            System.out.println("✓ Admin ya existe en la base de datos");
            return;
        }
        
        System.out.println("⚠ No hay admin en la BD. Creando admin por defecto...");
        
        Usuario admin = new Usuario();
        admin.setUser("admin");
        admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña por defecto (CAMBIAR EN PRODUCCIÓN)
        admin.setCorreo("admin@reverdecerpiura.com");
        admin.setNombres("Administrador");
        admin.setApellidoPaterno("Sistema");
        admin.setApellidoMaterno("ReVerdecer");
        admin.setDni("00000000");
        admin.setTipoDoc(TipoDoc.DNI);
        admin.setGenero("Otro");
        admin.setDireccion1("Sistema");
        admin.setActivo(true);
        admin.setIsAdmin(true);
        admin.setFechaRegistro(LocalDate.now());
        
        try {
            usuarioRepository.save(admin);
            System.out.println("✓ Admin creado exitosamente!");
            System.out.println("  - Usuario: admin");
            System.out.println("  - Correo: admin@reverdecerpiura.com");
            System.out.println("  - Contraseña: admin123 (CAMBIAR EN PRODUCCIÓN)");
        } catch (Exception e) {
            System.err.println("❌ Error al crear admin: " + e.getMessage());
        }
    }
}
